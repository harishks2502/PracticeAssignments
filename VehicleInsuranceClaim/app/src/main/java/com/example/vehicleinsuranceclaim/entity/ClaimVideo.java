package com.example.vehicleinsuranceclaim.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "claim_videos_table",
        foreignKeys = @ForeignKey(
                entity = AttachPolicy.class,
                parentColumns = "id",
                childColumns = "policyId",
                onDelete = ForeignKey.CASCADE
        )
)
public class ClaimVideo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int policyId; // Foreign key referencing AttachPolicy table

    @NonNull
    @ColumnInfo(name = "video_path")
    private String videoPath;

    public ClaimVideo(int policyId, @NonNull String videoPath) {
        this.policyId = policyId;
        this.videoPath = videoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    @NonNull
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(@NonNull String videoPath) {
        this.videoPath = videoPath;
    }

}
