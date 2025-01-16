package com.example.vehicleinsuranceclaim.service;

import com.example.vehicleinsuranceclaim.entity.NewUpdates;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    @GET("companies_list")
    Call<List<NewUpdates>> getNewUpdates();

}
