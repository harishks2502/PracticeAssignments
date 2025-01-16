package com.example.vehicleinsuranceclaim.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vehicleinsuranceclaim.entity.AttachPolicy;

import java.util.List;

@Dao
public interface AttachPolicyDao {

    @Insert
    void insertPolicy(AttachPolicy attachPolicy);

    @Update
    void updatePolicy(AttachPolicy attachPolicy);

    @Delete
    void deletePolicy(AttachPolicy attachPolicy);

    @Query("SELECT * FROM attachPolicy_table")
    List<AttachPolicy> getAllPolicies();

}
