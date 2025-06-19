package jp.ac.sanpoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Count extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previewTotalSteps = 0;
    private TextView stepView, pointView;
    private MaterialButton resetButton;

    private static final float POINT_CONVERSION_RATE = 0.1f;//covert 10 steps into 1 point

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        stepView = findViewById(R.id.stepView);
        pointView = findViewById(R.id.pointView);
        resetButton = findViewById(R.id.resetButton);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        loadData();
        updateUI();

        resetButton.setOnClickListener(v -> {
            previewTotalSteps = totalSteps;
            saveData();
            updateUI();
            Toast.makeText(Count.this, "歩数をリセットしました。", Toast.LENGTH_SHORT).show();
        });

        Button backtoMyPage = findViewById(R.id.backtoMyPage);
        backtoMyPage.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPage.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor == null) {
            Toast.makeText(this, "エラー歩数センサー！", Toast.LENGTH_SHORT).show();
        } else {
            mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            updateUI();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void updateUI() {
        int currentSteps = totalSteps - previewTotalSteps;
        if (currentSteps < 0) currentSteps = 0;

        stepView.setText("歩数: " + currentSteps);

        int points = Math.round(currentSteps * POINT_CONVERSION_RATE);
        pointView.setText("ポイント: " + points);

        saveStepsAndPoints(currentSteps, points);
    }

    private void saveData() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("key1", previewTotalSteps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        previewTotalSteps = sharedPref.getInt("key1", 0);
        totalSteps = previewTotalSteps;
    }

    private void saveStepsAndPoints(int steps, int points) {
        SharedPreferences sharedPref = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("steps", steps);
        editor.putInt("points", points);
        editor.apply();
    }
}
