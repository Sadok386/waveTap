package com.example.bonneannee.accelero;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private TextView mTxtViewX;
    private TextView mTxtViewY;
    private TextView mTxtViewZ;
    private TextView compteurTxt;
    private TextView conditionMaxTxt;
    private TextView textMaxZ;
    private Integer compteur = 0;
    private Float maxZ = 0f;
    private Float conditionMax = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        mTxtViewX = findViewById(R.id.textX);
        mTxtViewY = findViewById(R.id.textY);
        mTxtViewZ = findViewById(R.id.textZ);
        compteurTxt = findViewById(R.id.compteurText);
        textMaxZ = findViewById(R.id.maxZ);
        conditionMaxTxt = findViewById(R.id.conditionMax);
        long time = System.currentTimeMillis();
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void Position(float iX, float iY, float iZ)
    {
        mTxtViewZ.setText(" "+iZ);
        mTxtViewX.setText(" "+iX);
        mTxtViewY.setText(" "+iY);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            Position(x, y, z);
            if(z>conditionMax){
                compteur ++;
                compteurTxt.setText("taps = "+compteur);
                onPause();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                }, 1000);
            }
            if(z>maxZ){
                maxZ = z;
                textMaxZ.setText("max z = "+maxZ);



            }
        }

    }

    public void reinitialiserMax(View view){
        maxZ = 0f;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                conditionMax = maxZ;
                conditionMaxTxt.setText("condition = "+conditionMax);
            }
        }, 10000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
