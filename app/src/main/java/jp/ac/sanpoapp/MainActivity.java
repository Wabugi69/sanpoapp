package jp.ac.sanpoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.steps), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        PrefsManager prefs = new PrefsManager(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 100);
        }

        ExtendedFloatingActionButton registerButton = findViewById(R.id.registerButton);
        ExtendedFloatingActionButton existingAccountButton = findViewById(R.id.alreadyAccountButton);


        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this,Register.class));
        });

        existingAccountButton.setOnClickListener(v -> {
           startActivity(new Intent(this, Login.class));
        });
        //ログインしているならMyPageへ移る
        if (prefs.getToken() != null){
            startActivity(new Intent(this, MyPage.class));
        }
    }
}
