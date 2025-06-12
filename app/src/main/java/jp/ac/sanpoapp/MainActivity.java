package jp.ac.sanpoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PrefsManager prefs = new PrefsManager(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ExtendedFloatingActionButton registerButton = findViewById(R.id.registerButton);
        ExtendedFloatingActionButton existingAccountButton = findViewById(R.id.alreadyAccountButton);

        if (prefs.getToken() != null){
            startActivity(new Intent(this, MyPage.class));
        }
        else

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this,Register.class));
        });

        existingAccountButton.setOnClickListener(v -> {
           startActivity(new Intent(this, Login.class));
        });
    }
}
