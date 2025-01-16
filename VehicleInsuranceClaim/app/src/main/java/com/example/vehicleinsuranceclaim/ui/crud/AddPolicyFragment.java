package com.example.vehicleinsuranceclaim.ui.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.vehicleinsuranceclaim.dao.AttachPolicyDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;
import com.example.vehicleinsuranceclaim.notification.NotificationHelper;
import com.example.vehicleinsuranceclaim.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;


public class AddPolicyFragment extends Fragment {

    int userId;
    SharedPreferences sharedPreferences;
    EditText policyNumberField, vehicleNumberField, vehicleNameField;
    Button addPolicyButton;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_add_policy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);

        policyNumberField = view.findViewById(R.id.policyNumberField);
        vehicleNumberField = view.findViewById(R.id.vehicleNumberField);
        vehicleNameField = view.findViewById(R.id.vehicleNameField);
        addPolicyButton = view.findViewById(R.id.addPolicyButton);

        addPolicyButton.setOnClickListener(v -> {
            String policyNumber = policyNumberField.getText().toString().trim();
            String vehicleNumber = vehicleNumberField.getText().toString().trim();
            String vehicleName = vehicleNameField.getText().toString().trim();
            String claimStatus = "Not Applied";

            @SuppressLint("SimpleDateFormat")
            String dateSubmitted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            if (TextUtils.isEmpty(policyNumber) || TextUtils.isEmpty(vehicleNumber) || TextUtils.isEmpty(vehicleName)) {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    AttachPolicyDao attachPolicyDao = db.attachPolicyDao();

                    AttachPolicy attachPolicy = new AttachPolicy(userId,policyNumber, vehicleNumber, vehicleName, claimStatus, dateSubmitted);
                    attachPolicyDao.insertPolicy(attachPolicy);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Policy Attached Successfully!", Toast.LENGTH_SHORT).show();
                        NotificationHelper.createNotificationChannel(requireContext());
                        NotificationHelper.sendNotification(requireContext(), "Policy Added Successfully");
                        ((MainActivity) requireActivity()).replaceFragment(new AttachPolicyFragment(), "AttachPolicyFragment");
                    });
                });
            }

        });

    }

}