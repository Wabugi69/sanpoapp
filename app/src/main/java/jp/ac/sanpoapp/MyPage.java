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

        deleteAccountButton.setOnClickListener(v -> {
            prefs.clearToken();

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
