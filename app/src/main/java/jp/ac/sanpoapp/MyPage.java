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

public class MyPage extends AppCompatActivity {

    TextView accountInfo;
    Button myPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        accountInfo = findViewById(R.id.accountInfo);

        ExtendedFloatingActionButton  logoutButton= findViewById(R.id. logoutButton);
        ExtendedFloatingActionButton deleteAccountButton= findViewById(R.id.deleteAccountButton);


        PrefsManager prefs = new PrefsManager(this);

        if (prefs.getUsername() == null) {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        accountInfo.setText("Logged in as: " + prefs.getUsername());

        logoutButton.setOnClickListener(v -> {
            prefs.clearToken();

                Intent intent = new Intent(MyPage.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        });

        deleteAccountButton.setOnClickListener(v -> {
            String password;
//TODO FINISH UP THE METHOD TO CLEAR ACCOUNT FROM THE DB
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
//            Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MyPage.this, Register.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
        });
//        myPageButton = findViewById(R.id.myPageButton);
//        myPageButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, MyPage.class));
//        });
    }
}
