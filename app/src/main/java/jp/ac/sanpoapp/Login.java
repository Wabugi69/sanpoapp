package jp.ac.sanpoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    ImageView togglePasswordVisibility;
    CheckBox rememberMeCheckBox;
    boolean isPasswordVisible = false;
    PrefsManager prefs;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        ExtendedFloatingActionButton loginButton = findViewById(R.id.loginButton);
        ExtendedFloatingActionButton forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        Button Back = findViewById(R.id.Back);
        prefs = new PrefsManager(this);

        // Back button
        Back.setOnClickListener(v -> {
//            startActivity(new Intent(this, MainActivity.class));
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
            LoginUser(v);
//            String inputEmail = loginEmail.getText().toString().trim();
//            String inputPass = loginPassword.getText().toString().trim();
//
//            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//            String savedEmail = prefs.getString("email", "");
//            String savedPass = prefs.getString("password", "");
//
//            if (inputEmail.equals(savedEmail) && inputPass.equals(savedPass)) {
//                // Save login state
//                if (rememberMeCheckBox.isChecked()) {
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putBoolean("rememberMe", true);
//                    editor.apply();
//                }
//
//                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, MyPage.class));
//                finish();
//            } else {
//                Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show();
//            }
        });

        // Forgot password button
        forgotPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetPinActivity.class));
        });

        // Auto-fill
//        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        if (prefs.getBoolean("rememberMe", false)) {
//            loginEmail.setText(prefs.getString("email", ""));
//            loginPassword.setText(prefs.getString("password", ""));
//            rememberMeCheckBox.setChecked(true);
        }



    //ユーザーログイン機能
    public void LoginUser(View v) {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        new Thread(() -> {
            try {
                //APIに接続
                URL url = new URL("https://confirmed-sassy-trade.glitch.me/login");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setDoOutput(true);
                String jsonLoginString = String.format(
                        "{\"email\":\"%s\",\"password\":\"%s\"}", email, password
                );
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonLoginString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                //APIの返事を聞く
                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                //APIからのトーケンを受け取る
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                JSONObject json = new JSONObject(response.toString());
                String finalResponse = json.getString("message");

                //SharedPreferencesでトーケンを共有化する
                if (json.has("token")) {
                    String token = json.getString("token");
                    String username = json.getString("username");
                    prefs.saveToken(token, username);
                    startActivity(new Intent(this, MyPage.class));
                }

                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), finalResponse, Toast.LENGTH_LONG).show();
                });
            } catch (Exception e){
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "エラー： " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
