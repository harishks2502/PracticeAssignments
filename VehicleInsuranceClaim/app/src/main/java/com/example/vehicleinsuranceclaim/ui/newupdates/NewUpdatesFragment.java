package com.example.vehicleinsuranceclaim.ui.newupdates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.adapter.NewUpdatesAdapter;
import com.example.vehicleinsuranceclaim.entity.NewUpdates;
import com.example.vehicleinsuranceclaim.service.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewUpdatesFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater != null ? inflater.inflate(R.layout.fragment_new_updates, container, false) : null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.newUpdatesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://677a97bf671ca0306834584f.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //service object
        APIService apiService = retrofit.create(APIService.class);

        // Make the API call
        apiService.getNewUpdates().enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewUpdatesAdapter newUpdatesAdapter = new NewUpdatesAdapter((List<NewUpdates>) response.body());
                    recyclerView.setAdapter(newUpdatesAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("NewUpdatesFragment", "API call failed", t);
            }

        });

    }

}