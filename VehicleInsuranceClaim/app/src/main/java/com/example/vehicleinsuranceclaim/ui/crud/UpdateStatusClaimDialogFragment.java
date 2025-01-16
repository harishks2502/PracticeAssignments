package com.example.vehicleinsuranceclaim.ui.crud;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.adapter.PolicyAdapter;
import com.example.vehicleinsuranceclaim.dao.AttachPolicyDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;

import java.util.concurrent.Executors;


public class UpdateStatusClaimDialogFragment extends DialogFragment {

    private final AttachPolicy policy;
    private final PolicyAdapter policyAdapter;
    EditText updateClaimStatusField;
    Button updateClaimStatusButton;

    public UpdateStatusClaimDialogFragment(AttachPolicy policy, PolicyAdapter policyAdapter) {
        this.policy = policy;
        this.policyAdapter = policyAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_update_status_claim_dialog, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateClaimStatusField = view.findViewById(R.id.updateClaimStatusField);
        updateClaimStatusButton = view.findViewById(R.id.updateClaimStatusButton);

        updateClaimStatusField.setText(policy.getClaimStatus());

        updateClaimStatusButton.setOnClickListener(v -> {
            String claimStatus = updateClaimStatusField.getText().toString().trim();

            if (TextUtils.isEmpty(claimStatus)) {
                Toast.makeText(requireContext(), "Please enter the status!", Toast.LENGTH_SHORT).show();
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    AttachPolicyDao attachPolicyDao = db.attachPolicyDao();

                    policy.setClaimStatus(claimStatus);

                    attachPolicyDao.updatePolicy(policy);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Policy Status Updated Successfully!", Toast.LENGTH_SHORT).show();
                        policyAdapter.notifyDataSetChanged();
                        dismiss();
                    });
                });
            }

        });

    }
}