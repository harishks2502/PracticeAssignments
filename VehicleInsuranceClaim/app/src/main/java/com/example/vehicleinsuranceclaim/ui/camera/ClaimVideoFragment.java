package com.example.vehicleinsuranceclaim.ui.camera;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.PendingRecording;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.adapter.ClaimVideoAdapter;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;
import com.example.vehicleinsuranceclaim.entity.ClaimVideo;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClaimVideoFragment extends Fragment {

    private static final String TAG = "CameraXVideo";

    private final AttachPolicy policy;
    int policyId;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private PreviewView previewView;
    private Button recordButton;
    private ExecutorService cameraExecutor;
    private VideoCapture<Recorder> videoCapture;
    private Recording currentRecording;

    public ClaimVideoFragment(AttachPolicy policy) {
        this.policy = policy;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_claim_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        policyId = policy.getId();

        db = AppDatabase.getInstance(requireContext());

        recyclerView = view.findViewById(R.id.claimVideosRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        previewView = view.findViewById(R.id.previewView);
        recordButton = view.findViewById(R.id.recordButton);

        ActivityResultLauncher<String[]> permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean cameraGranted = result.get(Manifest.permission.CAMERA);
                    Boolean audioGranted = result.get(Manifest.permission.RECORD_AUDIO);

                    if (cameraGranted != null && cameraGranted && audioGranted != null && audioGranted) {
                        startCamera();
                    } else {
                        Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });

        permissionLauncher.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});

        cameraExecutor = Executors.newSingleThreadExecutor();

        loadVideos();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Recorder recorder = new Recorder.Builder()
                        .setExecutor(cameraExecutor)
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.bindToLifecycle(
                        this,
                        androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                        videoCapture,
                        preview
                );

                recordButton.setOnClickListener(view -> toggleRecording(recordButton));
            } catch (Exception e) {
                Log.e(TAG, "Use case binding failed", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void toggleRecording(Button recordButton) {
        if (currentRecording != null) {
            currentRecording.stop(); //Stop recording
            currentRecording = null;
            recordButton.setText("Record");
        } else {
            // Create video file
            File videoFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                    System.currentTimeMillis() + "_vehicle_damage.mp4");

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.getName());

            MediaStoreOutputOptions outputOptions = new MediaStoreOutputOptions.Builder(
                    requireContext().getContentResolver(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            ).setContentValues(contentValues).build();

            PendingRecording pendingRecording = videoCapture.getOutput().prepareRecording(requireContext(), outputOptions);
            currentRecording = pendingRecording.start(ContextCompat.getMainExecutor(requireContext()), event -> {
                if (event instanceof VideoRecordEvent.Start) {
                    recordButton.setText("Stop");
                    Log.d(TAG, "Recording started");
                } else if (event instanceof VideoRecordEvent.Finalize) {
                    recordButton.setText("Record");

                    VideoRecordEvent.Finalize finalizeEvent = (VideoRecordEvent.Finalize) event;

                    finalizeEvent.getOutputResults().getOutputUri();
                    String videoPath = finalizeEvent.getOutputResults().getOutputUri().toString();
                    saveVideoToDatabase(videoPath);

                    Log.d(TAG, "Recording finalized");
                }
            });
        }
    }

    private void saveVideoToDatabase(String videoPath) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ClaimVideo claimVideo = new ClaimVideo(policyId, videoPath);
            db.claimVideoDao().insertClaimVideo(claimVideo);
            Log.d(TAG, "Video saved to database: " + videoPath);
            loadVideos(); // Refresh the RecyclerView
        });
    }

    private void loadVideos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ClaimVideo> claimVideos = db.claimVideoDao().getClaimVideosByPolicyId(policyId);
            requireActivity().runOnUiThread(() -> {
                ClaimVideoAdapter adapter = new ClaimVideoAdapter(requireContext(), claimVideos);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }

}