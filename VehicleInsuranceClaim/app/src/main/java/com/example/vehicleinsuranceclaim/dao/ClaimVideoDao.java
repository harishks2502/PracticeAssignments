package com.example.vehicleinsuranceclaim.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.vehicleinsuranceclaim.entity.ClaimVideo;

import java.util.List;

@Dao
public interface ClaimVideoDao {
    @Insert
    void insertClaimVideo(ClaimVideo claimVideo);

    @Query("SELECT * FROM claim_videos_table WHERE policyId = :policyId")
    List<ClaimVideo> getClaimVideosByPolicyId(int policyId);

}
