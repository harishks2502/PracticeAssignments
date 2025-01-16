package com.example.vehicleinsuranceclaim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.entity.ClaimVideo;

import java.util.List;

public class ClaimVideoAdapter extends RecyclerView.Adapter<ClaimVideoAdapter.ClaimVideoViewHolder> {

    private final List<ClaimVideo> claimVideos;
    private final Context context;

    public ClaimVideoAdapter(Context context, List<ClaimVideo> claimVideos) {
        this.context = context;
        this.claimVideos = claimVideos;
    }

    @NonNull
    @Override
    public ClaimVideoAdapter.ClaimVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_claim_video, parent, false);
        return new ClaimVideoAdapter.ClaimVideoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimVideoAdapter.ClaimVideoViewHolder holder, int position) {
        ClaimVideo claimVideo = claimVideos.get(position);

        String videoPath = claimVideo.getVideoPath();

        if (videoPath != null && !videoPath.isEmpty()) {
            holder.videoView.setVideoPath(videoPath);
            holder.videoView.setOnPreparedListener(mediaPlayer -> holder.videoView.start());
            holder.videoView.setOnCompletionListener(mediaPlayer -> holder.videoView.seekTo(0));
            holder.videoView.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    holder.videoView.stopPlayback();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return claimVideos != null ? claimVideos.size() : 0;
    }

    public static class ClaimVideoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public ClaimVideoViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.claimVideoView);
        }

    }

}
