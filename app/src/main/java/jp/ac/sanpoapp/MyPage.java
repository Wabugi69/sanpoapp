package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyPage extends AppCompatActivity {

    TextView accountInfo;
    Button backToMyPage,myPageButton;
    PrefsManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        accountInfo = findViewById(R.id.accountInfo);
        backToMyPage = findViewById(R.id.backToMyPage);

        ExtendedFloatingActionButton  logoutButton= findViewById(R.id. logoutButton);
        ExtendedFloatingActionButton deleteAccountButton= findViewById(R.id.deleteAccountButton);


        backToMyPage.setOnClickListener(v -> {
            Intent intent = new Intent(MyPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        prefs = new PrefsManager(this);

        if (prefs.getToken() == null) {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        accountInfo.setText("Logged in as: ");

        logoutButton.setOnClickListener(v -> {
            prefs.clearToken();

            Intent intent = new Intent(MyPage.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        //TODO アカウントを削除するページを作成する、このボタンで遷移してパスワードを入れてもらうパスワードを入力して、トーケンと一緒にＡＰＩに送る。サーバーリスポンス次第でアカウントを削除する
        deleteAccountButton.setOnClickListener(v -> {
            String password;

            prefs.clearToken();
//
//            new Thread(() -> {
//                try {
//                    URL url = new URL("https://confirmed-sassy-trade.glitch.me/register");
//                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setDoOutput(true);
//
//                    String jsonInputString = String.format(
//                            "{\"password\":\"%s\"}", password;
//                    );
//
//                    try (OutputStream os = conn.getOutputStream()) {
//                        byte[] input = jsonInputString.getBytes("utf-8");
//                        os.write(input, 0, input.length);
//                    }
//                    int responseCode = conn.getResponseCode();
//                    InputStream is = (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST)
//                            ? conn.getInputStream()
//                            : conn.getErrorStream();
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//
//                    while ((line = br.readLine()) != null) {
//                        response.append(line.trim());
//                    }
//
//                    String finalResponse = response.toString();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(() ->
//                            Toast.makeText(getApplicationContext(), "エラー： " + e.getMessage(), Toast.LENGTH_LONG).show()
//                    );
//                }
//            }).start();

                    Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyPage.this, Register.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

        });
        myPageButton = findViewById(R.id.myPageButton);
        myPageButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPage.class));
        });
    }
}
