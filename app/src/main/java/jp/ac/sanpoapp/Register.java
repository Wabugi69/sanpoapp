package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button registerButton, alreadyAccountButton;
    ImageView togglePasswordVisibility;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        ExtendedFloatingActionButton  registerButton = findViewById(R.id. registerButton);
        ExtendedFloatingActionButton alreadyAccountButton = findViewById(R.id. alreadyAccountButton );

        Button BacktoMain = findViewById(R.id.BacktoMain);
        BacktoMain.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Toggle password
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
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Input username or password", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
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
//    public void RegisterUser(View v) {
//        String newEmail, newUsername, newPassword;
//
//        newEmail = emailInput.getText().toString();
//        newUsername = usernameInput.getText().toString();
//        newPassword = passwordInput.getText().toString();
//
//        new Thread(() -> {
//            try {
//                URL url = new URL("https://confirmed-sassy-trade.glitch.me/register");
//                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setDoOutput(true);
//
//                String jsonInputString = String.format(
//                        "{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}", newEmail,  newPassword, newUsername
//                );
//
//                try (OutputStream os = conn.getOutputStream()) {
//                    byte[] input = jsonInputString.getBytes("utf-8");
//                    os.write(input, 0, input.length);
//                }
//                int responseCode = conn.getResponseCode();
//                InputStream is = (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST)
//                        ? conn.getInputStream()
//                        : conn.getErrorStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = br.readLine()) != null) {
//                    response.append(line.trim());
//                }
//
//                String finalResponse = response.toString();
//
//                runOnUiThread(() -> {
//                    Toast.makeText(getApplicationContext(), finalResponse, Toast.LENGTH_LONG).show();
//                });
//            } catch (Exception e){
//                e.printStackTrace();
//                runOnUiThread(() ->
//                        Toast.makeText(getApplicationContext(), "エラー： " + e.getMessage(), Toast.LENGTH_LONG).show()
//                );
//            }
//        }).start();
//    }
}
