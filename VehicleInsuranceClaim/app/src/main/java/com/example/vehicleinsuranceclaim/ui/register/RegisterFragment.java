package com.example.vehicleinsuranceclaim.ui.register;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vehicleinsuranceclaim.R;
import com.example.vehicleinsuranceclaim.dao.UserDao;
import com.example.vehicleinsuranceclaim.database.AppDatabase;
import com.example.vehicleinsuranceclaim.entity.User;
import com.example.vehicleinsuranceclaim.ui.MainActivity;

import java.util.concurrent.Executors;

public class RegisterFragment extends Fragment {

    private EditText fullNameField, emailField, passwordField;
    private Button registerButton;
    private TextView clickText;
    //private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNameField = view.findViewById(R.id.fullNameField);
        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> registerUser());

        clickText = view.findViewById(R.id.clickText);
        clickText.setOnClickListener(v -> clickText());
    }

    private void registerUser() {
        String fullName = fullNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        registerButton.setEnabled(false); // Prevent multiple submissions

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            UserDao userDao = db.userDao();

            if (userDao.getUserByEmail(email) != null) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Email already registered", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                });
                return;
            }

            User user = new User(fullName, email, password);
            userDao.insertUser(user);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                registerButton.setEnabled(true);
                ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), "LoginFragment");
            });
        });
    }

    private void clickText() {
        ((MainActivity) requireActivity()).replaceFragment(new LoginFragment(), "LoginFragment");
    }

    //@Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        executorService.shutdown();
//    }

}