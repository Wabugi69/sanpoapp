package jp.ac.sanpoapp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.renderscript.Sampler;
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
    int currentSteps;
    private TextView stepView, pointView;
    private MaterialButton resetButton;
    PrefsManager prefs;
    int points;

    private static final float POINT_CONVERSION_RATE = 0.1f;//covert 10 steps into 1 point

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        prefs = new PrefsManager(this);
        stepView = findViewById(R.id.stepView);
        pointView = findViewById(R.id.pointView);
        resetButton = findViewById(R.id.resetButton);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        loadData();
        updateUI();

        resetButton.setOnClickListener(v -> {
            System.out.println(prefs.getPoints() + "   " + points);

            //Create animation to increase points
            ValueAnimator animator = ValueAnimator.ofInt(prefs.getPoints(), points + prefs.getPoints());
            animator.setDuration(1000); // 1 second
            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();
                pointView.setText("ポイント：　" + String.valueOf(animatedValue));
            });
            animator.start();

            //Create animation to decrease steps
            ValueAnimator stepAnimator = ValueAnimator.ofInt(currentSteps, 0);
            animator.setDuration(1000);
            animator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();
                stepView.setText("貯まった歩数：" + 0);
            });
            stepAnimator.start();
            pointView.animate()
                    .alpha(0.5f)
                    .setDuration(100)
                    .withEndAction(() -> pointView.animate().alpha(1f).setDuration(100));

            stepView.animate()
                    .alpha(0.5f)
                    .setDuration(100)
                            .withEndAction(() -> stepView.animate().alpha(1f).setDuration(100));
            //TODO FIX BUG WHERE MULTIPLE PRESSES RESETS POINTS TO 0 UNTIL PRESSED AGAIN
            prefs.updatePoints(this, (points + prefs.getPoints()));

            previewTotalSteps = totalSteps;
            saveData();
            updateUI();

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
        currentSteps = totalSteps - previewTotalSteps;
        if (currentSteps < 0) currentSteps = 0;

        stepView.setText("歩数: " + currentSteps);

        points = Math.round(currentSteps * POINT_CONVERSION_RATE);
        pointView.setText("ポイント: " + prefs.getPoints());

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
