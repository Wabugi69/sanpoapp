package jp.ac.sanpoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class LoginTest extends AppCompatActivity {
    private TextView tx1, tx2, tx3;
    public EditText emailText, usernameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        tx1 = findViewById(R.id.tx1);
        tx2 = findViewById(R.id.tx2);
        tx3 = findViewById(R.id.tx3);
    }

    //TODO Write a code to test registration. You can currently register from the terminal

    public void RegisterUser(View v) {
        String newEmail, newUsername, newPassword;

        newEmail = emailText.getText().toString();
        newUsername = usernameText.getText().toString();
        newPassword = passwordText.getText().toString();

        new Thread(() -> {
            try {
                URL url = new URL("https://confirmed-sassy-trade.glitch.me/register");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = String.format(
                        "{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}", newEmail,  newPassword, newUsername
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

                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "サーバーリスポンス：" + finalResponse, Toast.LENGTH_LONG).show();
                });
            } catch (Exception e){
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "エラー： " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
    
    public void GetTextFromSql(View v) {

        new Thread(() -> {
            DatabaseAPICalls apiCalls = new DatabaseAPICalls();
            apiCalls.fetchUsers(new DatabaseAPICalls.UsersCallback() {
                @Override
                public void onSuccess(ArrayList<RemoteAPI.User> users) {
                    runOnUiThread(() -> {
                        StringBuilder ids = new StringBuilder();
                        StringBuilder emails = new StringBuilder();
                        StringBuilder usernames = new StringBuilder();

                        for (RemoteAPI.User user : users) {
                            ids.append(user.id).append("\n");
                            emails.append(user.email).append("\n");
                            usernames.append(user.username).append("\n");
                        }

                        tx1.setText(ids.toString());
                        tx2.setText(emails.toString());
                        tx3.setText(usernames.toString());
                        Toast.makeText(getApplicationContext(), "Data loaded!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }).start();
    }

}