package com.example.vehicleinsuranceclaim.ui.camera;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.adapter.ClaimImageAdapter;
import com.example.vehicleinsuranceclaim.dao.ClaimImageDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;
import com.example.vehicleinsuranceclaim.entity.ClaimImage;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class ClaimImageFragment extends Fragment {

    private final AttachPolicy policy;
    int policyId;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private PreviewView previewView;
    private Button captureButton;
    private ClaimImageAdapter claimImageAdapter;
    private ProcessCameraProvider cameraProvider;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    public ClaimImageFragment(AttachPolicy policy) {
        this.policy = policy;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_claim_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        policyId = policy.getId();

        db = AppDatabase.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.claimImagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        previewView = view.findViewById(R.id.previewView);
        captureButton = view.findViewById(R.id.captureButton);

        claimImageAdapter = new ClaimImageAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(claimImageAdapter);

        displayImages(policyId);
        startCamera();

        try {
            checkAndRequestCameraPermission();
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageCapture imageCapture = new ImageCapture.Builder().build();
                cameraProviderFuture.get().bindToLifecycle(requireActivity(), cameraSelector, preview, imageCapture);

                captureButton.setOnClickListener(v -> takePicture(imageCapture));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    private void takePicture(ImageCapture imageCapture) {
        if (imageCapture == null) {
            Toast.makeText(requireContext(), "Camera not ready", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(requireContext(), "Camera ready", Toast.LENGTH_SHORT).show();
        }

        File photoFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "_vehicle_damage.jpg");

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                String savedUri = photoFile.getAbsolutePath();
                saveImageToDb(policyId, savedUri);
                displayImages(policyId);
                Toast.makeText(getContext(), "Image saved" + savedUri, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                exception.printStackTrace();
                Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void saveImageToDb(int policyId, String imagePath) {
        ClaimImageDao claimImageDao = db.claimImageDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            ClaimImage claimImage = new ClaimImage(policyId, imagePath);
            claimImageDao.insertClaimImage(claimImage);
            List<ClaimImage> updatedImages = claimImageDao.getClaimImagesByPolicyId(policyId);

            requireActivity().runOnUiThread(() -> {
                claimImageAdapter.updateData(updatedImages);
                claimImageAdapter.notifyDataSetChanged();
            });

        });

    }

    private void checkAndRequestCameraPermission() throws ExecutionException, InterruptedException {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    startCamera();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void displayImages(int policyId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ClaimImage> imagesList = new ArrayList<>();
            List<ClaimImage> images = db.claimImageDao().getClaimImagesByPolicyId(policyId);

            requireActivity().runOnUiThread(() -> {
                imagesList.addAll(images);
                recyclerView.setAdapter(claimImageAdapter);
                claimImageAdapter.notifyItemInserted(imagesList.size());
            });

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCamera();
    }

}

