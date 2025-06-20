package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class ResetPinActivity extends AppCompatActivity {

    EditText newPasswordInput, confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        ExtendedFloatingActionButton resetButton = findViewById(R.id.resetPasswordButton);

        //back to login
        Button backToLoginButton = findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
            finish();
        });

        resetButton.setOnClickListener(v -> {
            String newPass = newPasswordInput.getText().toString().trim();
            String confirmPass = confirmPasswordInput.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "全ての項目を入力してください！", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "パスワードが間違っています！", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password", newPass);
                editor.putBoolean("isLoggedIn", false);
                editor.putInt("loginAttempts", 0);
                editor.apply();

                Toast.makeText(this, "パスワードをリセットしました。", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Login.class));
                finish();
            }
        });
    }
}
