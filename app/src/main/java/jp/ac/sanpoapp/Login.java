package jp.ac.sanpoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    ImageView togglePasswordVisibility;
    CheckBox rememberMeCheckBox;
    boolean isPasswordVisible = false;

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

        // back to main
        Back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // toggle password visibility
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

        loginButton.setOnClickListener(v -> {
            String inputEmail = loginEmail.getText().toString().trim();
            String inputPass = loginPassword.getText().toString().trim();

            SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String savedEmail = prefs.getString("email", "");
            String savedPass = prefs.getString("password", "");

            if (inputEmail.equals(savedEmail) && inputPass.equals(savedPass)) {
                // save login state
                if (rememberMeCheckBox.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("rememberMe", true);
                    editor.apply();
                }

                Toast.makeText(this, "ログインしました。", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MyPage.class));
                finish();
            } else {
                Toast.makeText(this, "入力に間違いがあります！", Toast.LENGTH_SHORT).show();
            }
        });

        // to reset pin
        forgotPasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetPinActivity.class));
        });

        // info auto-fill
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("rememberMe", false)) {
            loginEmail.setText(prefs.getString("email", ""));
            loginPassword.setText(prefs.getString("password", ""));
            rememberMeCheckBox.setChecked(true);
        }
    }
}
