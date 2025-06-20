package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    ImageView togglePasswordVisibility;
    CheckBox rememberMeCheckBox;
    boolean isPasswordVisible = false;
    PrefsManager prefs;


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
            LoginUser(v);
        });

        // Forgot password button
        forgotPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetPinActivity.class));
        });
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
                br.close();

                JSONObject json = new JSONObject(response.toString());
                String finalResponse = json.getString("message");

                //SharedPreferencesでトーケンを共有化する
                if (json.has("token")) {
                    String token = json.getString("token");
                    String username = json.getString("username");
                    int points = json.getInt("points");
                    prefs.saveToken(token, username, points);
                    startActivity(new Intent(this, MyPage.class));
                    finish();
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
