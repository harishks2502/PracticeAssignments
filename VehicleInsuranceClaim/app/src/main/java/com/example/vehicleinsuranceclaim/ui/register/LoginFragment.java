package com.example.vehicleinsuranceclaim.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.dao.UserDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.User;
import com.example.vehicleinsuranceclaim.ui.DashboardFragment;
import com.example.vehicleinsuranceclaim.ui.MainActivity;

import java.util.concurrent.Executors;


public class LoginFragment extends Fragment {

    private EditText emailField, passwordField;
    protected Button loginButton, registerButton;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> registerUser());

    }

    public void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            UserDao userDao = db.userDao();

            User user = userDao.getUser(email, password);

            requireActivity().runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                    sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("FULL_NAME", user.getFullName());
                    editor.putInt("USER_ID", user.getId());
                    editor.apply();

                    ((MainActivity) requireActivity()).replaceFragment(new DashboardFragment(), "DashboardFragment");
                } else {
                    Toast.makeText(requireContext(), "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    public void registerUser() {
        ((MainActivity) requireActivity()).replaceFragment(new RegisterFragment(), "RegisterFragment");
    }

}