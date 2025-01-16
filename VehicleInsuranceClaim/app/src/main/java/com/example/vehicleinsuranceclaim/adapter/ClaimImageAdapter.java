package com.example.vehicleinsuranceclaim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.entity.ClaimImage;

import java.util.List;

public class ClaimImageAdapter extends RecyclerView.Adapter<ClaimImageAdapter.ClaimImageViewHolder> {

    private final List<ClaimImage> claimImages;
    private final Context context;

    public ClaimImageAdapter(Context context, List<ClaimImage> claimImages) {
        this.context = context;
        this.claimImages = claimImages;
    }

    @NonNull
    @Override
    public ClaimImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_claim_image, parent, false);
        return new ClaimImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimImageViewHolder holder, int position) {
        ClaimImage claimImage = claimImages.get(position);

        String imagePath = claimImage.getImagePath();

        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(context)
                    .load(imagePath)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return claimImages != null ? claimImages.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ClaimImage> newImages) {
        claimImages.clear();
        claimImages.addAll(newImages);
        notifyDataSetChanged();
    }

    public static class ClaimImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ClaimImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.claimImageView);
        }

    }

}
