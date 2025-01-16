package com.example.vehicleinsuranceclaim.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.ui.toolbar.AboutFragment;
import com.example.vehicleinsuranceclaim.ui.toolbar.SettingsFragment;
import com.example.vehicleinsuranceclaim.ui.crud.AddPolicyFragment;
import com.example.vehicleinsuranceclaim.ui.crud.AttachPolicyFragment;
import com.example.vehicleinsuranceclaim.ui.map.MapFragment;
import com.example.vehicleinsuranceclaim.ui.newupdates.NewUpdatesFragment;
import com.example.vehicleinsuranceclaim.ui.register.LoginFragment;

public class DashboardFragment extends Fragment {

    String fullName;
    TextView titleTextView;
    Button logoutButton, attachPolicyButton;
    LinearLayout myPolicySection, mapSection, newUpdatesSection;
    SharedPreferences sharedPreferences;
    ImageView supportSms, supportCall;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private String emergencyMessage = "Emergency! Please help. Location: Office.";
    private String emergencyContact = "+918073350431";

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        titleTextView = view.findViewById(R.id.titleTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        myPolicySection = view.findViewById(R.id.myPolicySection);
        mapSection = view.findViewById(R.id.mapSection);
        newUpdatesSection = view.findViewById(R.id.newUpdatesSection);
        attachPolicyButton = view.findViewById(R.id.attachPolicyButton);
        supportSms = view.findViewById(R.id.supportSms);
        supportCall = view.findViewById(R.id.supportCall);

        sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        fullName = sharedPreferences.getString("FULL_NAME", "Guest");

        titleTextView.setText("Welcome, " + fullName + "..!");

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), "LoginFragment");
        });

        myPolicySection.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new AttachPolicyFragment(), "AttachPolicyFragment");
        });

        mapSection.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new MapFragment(), "MapFragment");
        });

        newUpdatesSection.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new NewUpdatesFragment(), "NewUpdatesFragment");
        });

        attachPolicyButton.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new AddPolicyFragment(), "AttachPolicyFragment");
        });

        supportSms.setOnClickListener(v -> sendEmergencySms());
        supportCall.setOnClickListener(v -> makeEmergencyCall());

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase("settings")) {
            Toast.makeText(getContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).replaceFragment(new SettingsFragment(), "SettingsFragment");
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase("about app")) {
            Toast.makeText(getContext(), "About App clicked", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).replaceFragment(new AboutFragment(), "AboutFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmergencySms() {
        if (checkPermissions(Manifest.permission.SEND_SMS)) {
            if (isNetworkAvailable()) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(emergencyContact, null, emergencyMessage, null, null);
                Toast.makeText(getActivity(), "Emergency SMS sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No network available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS});
        }
    }

    private void makeEmergencyCall() {
        if (checkPermissions(Manifest.permission.CALL_PHONE)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + emergencyContact));
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE});
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private boolean checkPermissions(String permission) {
        return ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(String[] permissions) {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}