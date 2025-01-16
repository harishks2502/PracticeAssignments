package com.example.vehicleinsuranceclaim.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "attachPolicy_table",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class AttachPolicy {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId; // Foreign key referencing User table

    @ColumnInfo(name = "policy_number")
    private String policyNumber;

    @ColumnInfo(name = "vehicle_number")
    private String vehicleNumber;

    @ColumnInfo(name = "vehicle_name")
    private String vehicleName;

    @ColumnInfo(name = "claim_status")
    private String claimStatus;

    @ColumnInfo(name = "date_submitted")
    private String dateSubmitted;

    public AttachPolicy(int userId, String policyNumber, String vehicleNumber, String vehicleName, String claimStatus, String dateSubmitted) {
        this.userId = userId;
        this.policyNumber = policyNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleName = vehicleName;
        this.claimStatus = claimStatus;
        this.dateSubmitted = dateSubmitted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

}