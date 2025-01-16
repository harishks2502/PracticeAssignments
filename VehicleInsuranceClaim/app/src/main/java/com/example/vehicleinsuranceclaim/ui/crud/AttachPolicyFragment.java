package com.example.vehicleinsuranceclaim.ui.crud;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.adapter.PolicyAdapter;
import com.example.vehicleinsuranceclaim.dao.AttachPolicyDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;
import com.example.vehicleinsuranceclaim.notification.NotificationHelper;
import com.example.vehicleinsuranceclaim.ui.camera.ClaimImageFragment;
import com.example.vehicleinsuranceclaim.ui.MainActivity;
import com.example.vehicleinsuranceclaim.ui.camera.ClaimVideoFragment;

import java.util.List;
import java.util.concurrent.Executors;


public class AttachPolicyFragment extends Fragment {

    Button attachPolicyButton;
    private RecyclerView recyclerView;
    private List<AttachPolicy> policies;
    private PolicyAdapter policyAdapter;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_attach_policy, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachPolicyButton = view.findViewById(R.id.attachPolicyButton);
        attachPolicyButton.setOnClickListener(v -> attachPolicy());

        recyclerView = view.findViewById(R.id.myPoliciesRecyclerView);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this.requireContext());
            policies = db.attachPolicyDao().getAllPolicies();

            requireActivity().runOnUiThread(() -> {
                policyAdapter = new PolicyAdapter(policies, this::updateClaimStatus, this::uploadClaimImage, this::uploadClaimVideo, this::updatePolicy, this::deletePolicy);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(policyAdapter);
                policyAdapter.notifyDataSetChanged();
            });

        });

    }

    private void attachPolicy() {
        ((MainActivity) requireActivity()).replaceFragment(new AddPolicyFragment(), "AddPolicyFragment");
    }

    private void updateClaimStatus(AttachPolicy policy) {
        new UpdateStatusClaimDialogFragment(policy, policyAdapter).show(getParentFragmentManager(), "UpdateStatusClaimDialogFragment");
    }

    private void uploadClaimImage(AttachPolicy policy) {
        ((MainActivity) requireActivity()).replaceFragment(new ClaimImageFragment(policy), "ClaimImageFragment");
    }

    private void uploadClaimVideo(AttachPolicy policy) {
        ((MainActivity) requireActivity()).replaceFragment(new ClaimVideoFragment(policy), "ClaimVideoFragment");
    }

    private void updatePolicy(AttachPolicy policy) {
        ((MainActivity) requireActivity()).replaceFragment(new UpdatePolicyFragment(policy, policyAdapter), "EditPolicyFragment");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deletePolicy(AttachPolicy attachPolicy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.requireContext());
        builder.setTitle("Delete Policy");
        builder.setMessage("Are you sure you want to delete this policy?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this.requireContext());
                AttachPolicyDao attachPolicyDao = db.attachPolicyDao();
                attachPolicyDao.deletePolicy(attachPolicy);

                requireActivity().runOnUiThread(() -> {
                    policies.remove(attachPolicy);
                    Toast.makeText(requireContext(), "Policy Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    NotificationHelper.createNotificationChannel(requireContext());
                    NotificationHelper.sendNotification(requireContext(), "Policy Deleted Successfully");
                    policyAdapter.notifyDataSetChanged();
                });
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}