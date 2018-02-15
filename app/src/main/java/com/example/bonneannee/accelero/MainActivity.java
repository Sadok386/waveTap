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
    private TextView outputText;
    private TextView textMaxZ;
    private Integer compteur = 0;
    private Float maxZ = 0f;
    private Float conditionMax = 0f;
    private Tap tap1;
    private Tap tap2;
    private long delta;
    private boolean triple = false;
    private String lettre = "";

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
        outputText = findViewById(R.id.outputText);
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
            long time = System.currentTimeMillis();
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
                }, 500);

                if(tap1 == null){
                    tap1 = new Tap(z, time);
                }else{
                    tap2 = new Tap(z, time);
                    delta = tap2.getTime() - tap1.getTime();
                    decodeDelta(delta);
                    tap1 = tap2;
                }

            }
            if(z>maxZ){
                maxZ = z;
                textMaxZ.setText("max z = "+maxZ);
            }
        }

    }

    public void decodeDelta(long delta){
        if(triple){
            if(delta>800 & delta<1200){
                outputText.setText(outputText.getText()+" "+ delta +" long ");
                lettre += "l";
            }else if(delta>1800 & delta<2200){
                outputText.setText(outputText.getText()+";");
                triple = false;
            }else if(delta>3800 & delta<4200){
                outputText.setText(outputText.getText()+" "+ delta+" FL;");
                decodeLettre(lettre);
                lettre = "";
                triple = false;
            }else if(delta>7800 & delta<8200){
                outputText.setText(outputText.getText()+" "+ delta+" FM;");
                decodeLettre(lettre);
                lettre = "";
                triple = false;
            }
        }else{
            if(delta>800 & delta<1200){
                triple = true;
            }else if(delta>1800 & delta<2200){
                outputText.setText(outputText.getText()+" "+ delta +" court ;");
                lettre += "c";
            }else if(delta>3800 & delta<4200){
                outputText.setText(outputText.getText()+" "+ delta+" court FL;");
                lettre += "c";
                decodeLettre(lettre);
                lettre = "";
            }else if(delta>7800 & delta<8200){
                outputText.setText(outputText.getText()+" "+ delta+" court FL FM;");
                lettre += "c";
                decodeLettre(lettre);
                lettre = "";
            }
        }
    }

    public void decodeLettre(String lettre){

        switch(lettre){
            case "cl":
                outputText.setText(outputText.getText()+"A");
                break;
            case "lccc":
                outputText.setText(outputText.getText()+"B");
                break;
            case "lclc":
                outputText.setText(outputText.getText()+"C");
                break;
            case "lcc":
                outputText.setText(outputText.getText()+"D");
                break;
            case "c":
                outputText.setText(outputText.getText()+"E");
                break;
            case "cclc":
                outputText.setText(outputText.getText()+"F");
                break;
            case "llc":
                outputText.setText(outputText.getText()+"G");
                break;
            case "cccc":
                outputText.setText(outputText.getText()+"H");
                break;
            case "cc":
                outputText.setText(outputText.getText()+"I");
                break;
            case "clll":
                outputText.setText(outputText.getText()+"J");
                break;
            case "lcl":
                outputText.setText(outputText.getText()+"K");
                break;
            case "clcc":
                outputText.setText(outputText.getText()+"L");
                break;
            case "ll":
                outputText.setText(outputText.getText()+"M");
                break;
            case "lc":
                outputText.setText(outputText.getText()+"N");
                break;
            case "lll":
                outputText.setText(outputText.getText()+"O");
                break;
            case "cllc":
                outputText.setText(outputText.getText()+"p");
                break;
            case "llcl":
                outputText.setText(outputText.getText()+"Q");
                break;
            case "clc":
                outputText.setText(outputText.getText()+"R");
                break;
            case "ccc":
                outputText.setText(outputText.getText()+"S");
                break;
            case "l":
                outputText.setText(outputText.getText()+"T");
                break;
            case "ccl":
                outputText.setText(outputText.getText()+"U");
                break;
            case "cccl":
                outputText.setText(outputText.getText()+"V");
                break;
            case "cll":
                outputText.setText(outputText.getText()+"W");
                break;
            case "lccl":
                outputText.setText(outputText.getText()+"X");
                break;
            case "lcll":
                outputText.setText(outputText.getText()+"Y");
                break;
            case "llcc":
                outputText.setText(outputText.getText()+"Z");
                break;

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
                outputText.setText("");
                triple = false;
                tap1 = null;
                tap2 = null;
            }
        }, 10000);
        lettre = "";
    }

    public void stopPhrase(View view){
        if(triple){
            outputText.setText(outputText.getText()+" FL FM;");
            decodeLettre(lettre);
            lettre = "";
        }else{
            outputText.setText(outputText.getText()+"court FL FM;");
            lettre += "c";
            decodeLettre(lettre);
            lettre = "";
        }
        triple = false;
        tap1 = null;
        tap2 = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
