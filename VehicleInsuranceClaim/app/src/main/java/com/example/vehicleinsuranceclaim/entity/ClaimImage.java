package com.example.vehicleinsuranceclaim.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "claim_images_table",
        foreignKeys = @ForeignKey(
                entity = AttachPolicy.class,
                parentColumns = "id",
                childColumns = "policyId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("policyId")
)
public class ClaimImage {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private final int policyId; // Foreign key referencing User table

    @ColumnInfo(name = "image_path")
    private String imagePath;

    public ClaimImage(int policyId, String imagePath) {
        this.policyId = policyId;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

}
