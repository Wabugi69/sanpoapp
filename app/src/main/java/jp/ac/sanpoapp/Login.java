package jp.ac.sanpoapp;

import android.annotation.SuppressLint;
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

public class Login extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    ImageView togglePasswordVisibility;
    boolean isPasswordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        ExtendedFloatingActionButton loginButton = findViewById(R.id.loginButton);
        ExtendedFloatingActionButton forgotPasswordButton= findViewById(R.id.forgotPasswordButton);

        Button Back= findViewById(R.id.Back);
        Back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_off_24);
            } else {
                loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(R.drawable.baseline_visibility_24);
            }
            isPasswordVisible = !isPasswordVisible;
            loginPassword.setSelection(loginPassword.length());
        });

        // Login button
        loginButton.setOnClickListener(v -> {
            String inputUser = loginUsername.getText().toString().trim();
            String inputPass = loginPassword.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String savedUser = prefs.getString("username", "");
            String savedPass = prefs.getString("password", "");

            if (inputUser.equals(savedUser) && inputPass.equals(savedPass)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MyPage.class));
                finish();
            } else {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Forgot password
        forgotPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetPinActivity.class));
        });
    }
//    public void LoginUser(View v) {
//        String email = emailInput.getText().toString();
//        String password = passwordInput.getText().toString();
//
//        new Thread(() -> {
//            try {
//                URL url = new URL("https://confirmed-sassy-trade.glitch.me/login");
//                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-type", "application/json");
//                conn.setDoOutput(true);
//                String jsonLoginString = String.format(
//                        "{\"email\":\"%s\",\"password\":\"%s\"}", email, password
//                );
//                try {
//                    OutputStream os = conn.getOutputStream()) {
//                        byte[] input =jsonLoginString.getBytes("utf-8");
//                        os.write(input, 0, input.length);
//                    }
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
