package com.example.vehicleinsuranceclaim.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vehicleinsuranceclaim.entity.ClaimImage;

import java.util.List;

@Dao
public interface ClaimImageDao {
    @Insert
    void insertClaimImage(ClaimImage claimImage);

    @Query("SELECT * FROM claim_images_table WHERE policyId = :policyId")
    List<ClaimImage> getClaimImagesByPolicyId(int policyId);

}
