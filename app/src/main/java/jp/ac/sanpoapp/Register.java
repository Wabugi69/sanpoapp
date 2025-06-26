package jp.ac.sanpoapp;
import android.content.Intent;
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

    EditText emailInput, usernameInput, passwordInput;
    ImageView togglePasswordVisibility;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.loginEmail);
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
        registerButton.setOnClickListener(v -> {
            RegisterUser(v);
        });
    }

        public void RegisterUser (View v){
            String newEmail, newUsername, newPassword;

            newEmail = emailInput.getText().toString();
            newUsername = usernameInput.getText().toString();
            newPassword = passwordInput.getText().toString();

            new Thread(() -> {
                try {
                    URL url = new URL("https://confirmed-sassy-trade.glitch.me/register");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    String jsonInputString = String.format(
                            "{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}", newEmail, newPassword, newUsername
                    );

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                    int responseCode = conn.getResponseCode();
                    InputStream is = (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    String finalResponse = response.toString();
                    //TODO UPON ACCOUNT CREATION, LOGIN USER AND SHOW A WELCOME SCREEN
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), finalResponse, Toast.LENGTH_LONG).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "エラー： " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
    }
}
