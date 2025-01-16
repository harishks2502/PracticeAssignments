package com.example.vehicleinsuranceclaim.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.entity.AttachPolicy;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder> {

    private final List<AttachPolicy> policies;
    private final UpdateClaimStatus onUpdateClaimStatus;
    private final UploadClaimImage onUploadClaimImage;

    private final UploadClaimVideo onUploadClaimVideo;
    private final UpdatePolicy onUpdatePolicy;
    private final DeletePolicy onDeletePolicy;

    public interface UpdateClaimStatus {
        void onUpdateClaimStatus(AttachPolicy policy);
    }

    public interface UploadClaimImage {
        void onUploadClaimImage(AttachPolicy policy);
    }

    public interface UploadClaimVideo {
        void onUploadClaimVideo(AttachPolicy policy);
    }

    public interface UpdatePolicy {
        void onUpdatePolicy(AttachPolicy policy);
    }

    public interface DeletePolicy {
        void onDeletePolicy(AttachPolicy policy);
    }

    public PolicyAdapter(List<AttachPolicy> policies, UpdateClaimStatus updateClaimStatus, UploadClaimImage uploadClaimImage, UploadClaimVideo uploadClaimVideo, UpdatePolicy updatePolicy, DeletePolicy deletePolicy) {
        this.policies = policies;
        this.onUpdateClaimStatus = updateClaimStatus;
        this.onUploadClaimImage = uploadClaimImage;
        this.onUploadClaimVideo = uploadClaimVideo;
        this.onUpdatePolicy = updatePolicy;
        this.onDeletePolicy = deletePolicy;
    }

    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_policy, parent, false);
        return new PolicyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, int position) {
        AttachPolicy policy = policies.get(position);
        holder.policyNumber.setText("Policy Number: " + policy.getPolicyNumber());
        holder.vehicleNumber.setText("Vehicle Number: " + policy.getVehicleNumber());
        holder.vehicleName.setText("Vehicle Name: " + policy.getVehicleName());
        holder.purchaseDate.setText("Purchase Date: " + policy.getDateSubmitted());
        holder.claimStatus.setText("Claim Status: " + policy.getClaimStatus());
        holder.updateClaimStatusButton.setOnClickListener(v -> {
            if (onUpdateClaimStatus != null) {
                onUpdateClaimStatus.onUpdateClaimStatus(policy);
            }
        });
        holder.cameraButton.setOnClickListener(v -> {
            if (onUploadClaimImage != null) {
                onUploadClaimImage.onUploadClaimImage(policy);
            }
        });
        holder.videoButton.setOnClickListener(v -> {
            if (onUploadClaimVideo != null) {
                onUploadClaimVideo.onUploadClaimVideo(policy);
            }
        });
        holder.updateButton.setOnClickListener(v -> {
            if (onUpdatePolicy != null) {
                onUpdatePolicy.onUpdatePolicy(policy);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (onDeletePolicy != null) {
                onDeletePolicy.onDeletePolicy(policy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return policies != null ? policies.size() : 0;
    }

    public static class PolicyViewHolder extends RecyclerView.ViewHolder {
        TextView policyNumber, vehicleNumber, vehicleName, purchaseDate, claimStatus;
        Button updateClaimStatusButton, cameraButton, videoButton, updateButton, deleteButton;

        public PolicyViewHolder(@NonNull View itemView) {
            super(itemView);
            policyNumber = itemView.findViewById(R.id.policyNumber);
            vehicleNumber = itemView.findViewById(R.id.vehicleNumber);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            purchaseDate = itemView.findViewById(R.id.purchaseDate);
            claimStatus = itemView.findViewById(R.id.claimStatus);
            updateClaimStatusButton = itemView.findViewById(R.id.updateClaimStatusButton);
            cameraButton = itemView.findViewById(R.id.cameraButton);
            videoButton = itemView.findViewById(R.id.videoButton);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

    }

}
