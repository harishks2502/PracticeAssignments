package com.example.vehicleinsuranceclaim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.entity.NewUpdates;

import java.util.List;

public class NewUpdatesAdapter extends RecyclerView.Adapter<NewUpdatesAdapter.NewUpdatesViewHolder> {
    private final List<NewUpdates> newUpdatesList;

    public NewUpdatesAdapter(List<NewUpdates> newUpdatesList) {
        this.newUpdatesList = newUpdatesList;
    }

    @NonNull
    @Override
    public NewUpdatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_new_updates_item, parent, false);
        return new NewUpdatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewUpdatesViewHolder holder, int position) {
        NewUpdates newUpdates = newUpdatesList.get(position);
        holder.nameTextView.setText("Company Name: " + newUpdates.getName());
        holder.descriptionTextView.setText("Description: " + newUpdates.getDescription());
        holder.contactTextView.setText("Contact: " + newUpdates.getContact());
    }

    @Override
    public int getItemCount() {
        return newUpdatesList != null ? newUpdatesList.size() : 0;
    }

    public static class NewUpdatesViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, contactTextView;

        public NewUpdatesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
        }

    }

}