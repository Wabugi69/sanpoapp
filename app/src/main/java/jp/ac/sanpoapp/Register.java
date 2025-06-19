package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Register extends AppCompatActivity {

    EditText loginEmail, usernameInput, passwordInput;
    ImageView togglePasswordVisibility;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginEmail = findViewById(R.id.loginEmail);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        ExtendedFloatingActionButton registerButton = findViewById(R.id.registerButton);
        ExtendedFloatingActionButton alreadyAccountButton = findViewById(R.id.alreadyAccountButton);

        Button backToMain = findViewById(R.id.BacktoMain);
        backToMain.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24);
            } else {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24);
            }
            isPasswordVisible = !isPasswordVisible;
            passwordInput.setSelection(passwordInput.length());
        });

        // Register new account
        registerButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", email);
                editor.putString("username", username);
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Login.class));
                finish();
            }
        });

        // Use existing account
        alreadyAccountButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String savedUsername = prefs.getString("username", null);
            String savedPassword = prefs.getString("password", null);

            if (savedUsername != null && savedPassword != null) {
                startActivity(new Intent(this, Login.class));
                finish();
            } else {
                Toast.makeText(this, "No existing account found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
