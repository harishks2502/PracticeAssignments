package com.example.vehicleinsuranceclaim.ui.crud;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.vehicleinsuranceclaim.notification.NotificationHelper;
import com.example.vehicleinsuranceclaim.ui.MainActivity;

import java.util.concurrent.Executors;

public class UpdatePolicyFragment extends Fragment {

    EditText policyNumberField, vehicleNumberField, vehicleNameField, claimStatusField;
    Button updatePolicyButton;
    private final AttachPolicy policy;
    private final PolicyAdapter policyAdapter;

    public UpdatePolicyFragment(AttachPolicy policy, PolicyAdapter policyAdapter) {
        this.policy = policy;
        this.policyAdapter = policyAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_update_policy, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        policyNumberField = view.findViewById(R.id.policyNumberField);
        vehicleNumberField = view.findViewById(R.id.vehicleNumberField);
        vehicleNameField = view.findViewById(R.id.vehicleNameField);
        claimStatusField = view.findViewById(R.id.claimStatusField);
        updatePolicyButton = view.findViewById(R.id.updatePolicyButton);

        policyNumberField.setText(policy.getPolicyNumber());
        vehicleNumberField.setText(policy.getVehicleNumber());
        vehicleNameField.setText(policy.getVehicleName());
        claimStatusField.setText(policy.getClaimStatus());

        updatePolicyButton.setOnClickListener(v -> {
            String policyNumber = policyNumberField.getText().toString().trim();
            String vehicleNumber = vehicleNumberField.getText().toString().trim();
            String vehicleName = vehicleNameField.getText().toString().trim();
            String claimStatus = claimStatusField.getText().toString().trim();

            if (TextUtils.isEmpty(policyNumber) || TextUtils.isEmpty(vehicleNumber) || TextUtils.isEmpty(vehicleName) || TextUtils.isEmpty(claimStatus)) {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    AttachPolicyDao attachPolicyDao = db.attachPolicyDao();

                    policy.setPolicyNumber(policyNumber);
                    policy.setVehicleNumber(vehicleNumber);
                    policy.setVehicleName(vehicleName);
                    policy.setClaimStatus(claimStatus);

                    attachPolicyDao.updatePolicy(policy);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Policy Updated Successfully!", Toast.LENGTH_SHORT).show();
                        NotificationHelper.createNotificationChannel(requireContext());
                        NotificationHelper.sendNotification(requireContext(), "Policy Updated Successfully");
                        policyAdapter.notifyDataSetChanged();
                        ((MainActivity) requireActivity()).replaceFragment(new AttachPolicyFragment(), "AttachPolicyFragment");
                    });
                });
            }
        });

    }

}