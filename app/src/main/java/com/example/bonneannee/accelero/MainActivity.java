package com.example.bonneannee.accelero;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float tempo = Float.parseFloat("1");
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private TextView tempoText;
    private TextView mTxtViewZ;
    private TextView compteurTxt;
    private TextView conditionMaxTxt;
    private TextView outputText;
    private TextView textReponse;
    private TextView textMaxZ;
    private EditText editTempo;
    private Integer compteur = 0;
    private Float maxZ = 0f;
    private Float conditionMax = 0f;
    private Tap tap1;
    private Tap tap2;
    private long delta;
    private boolean triple = false;
    private String lettre = "";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        textReponse = findViewById(R.id.textReponse);
        mTxtViewZ = findViewById(R.id.textZ);
        compteurTxt = findViewById(R.id.compteurText);
        textMaxZ = findViewById(R.id.maxZ);
        editTempo = findViewById(R.id.editTempo);
        conditionMaxTxt = findViewById(R.id.conditionMax);
        outputText = findViewById(R.id.outputText);
        tempoText = findViewById(R.id.textTempo);
        tempoText.setText("Tempo : " + tempo + "s");

        TextView myText = (TextView) findViewById(R.id.flash);

        ObjectAnimator anim = ObjectAnimator.ofInt(myText, "backgroundColor", Color.WHITE, Color.WHITE,
                Color.RED, Color.WHITE, Color.WHITE);
        anim.setDuration(longValue());
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
    @SuppressLint("WrongConstant")
    public void changeTempo(View view){
        String rep = editTempo.getText().toString();
        if (rep != null){
            tempo = Float.parseFloat(rep);
            tempoText.setText("tempo : "+tempo+"s");
            TextView myText = (TextView) findViewById(R.id.flash);

            ObjectAnimator anim = ObjectAnimator.ofInt(myText, "backgroundColor", Color.WHITE, Color.WHITE,
                    Color.RED, Color.WHITE, Color.WHITE);
            anim.setDuration(longValue());
            anim.setEvaluator(new ArgbEvaluator());
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            anim.start();
        }
    }
    public long longValue() {
        return (long)(tempo*1000.00);
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
                compteurTxt.setText("Tap: "+compteur);

                onPause();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onResume();
                    }
                }, 300);

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
                textMaxZ.setText("Z max= "+maxZ);
            }
        }

    }

    public void decodeDelta(long delta){
        if(triple){
            if(delta>((tempo*1000)-200) & delta<((tempo*1000)+200)){
                outputText.setText(outputText.getText()+" ▬ ");
                lettre += "l";
            }else if(delta>((tempo*2000)-200) & delta<((tempo*2000)+200)){
               // outputText.setText(outputText.getText()+";");
                triple = false;
            }else if(delta>((tempo*4000)-200) & delta<((tempo*4000)+200)){
                outputText.setText(outputText.getText()+"");
                decodeLettre(lettre);
                lettre = "";
                triple = false;
            }else if(delta>((tempo*8000)-200) & delta<((tempo*8000)+200)){
                outputText.setText(outputText.getText()+"~");
                decodeLettre(lettre);
                textReponse.setText(textReponse.getText()+"");
                lettre = "";
                triple = false;
            }
        }else{
            if(delta>((tempo*1000)-200) & delta<((tempo*1000)+200)){
                triple = true;
            }else if(delta>((tempo*2000)-200) & delta<((tempo*2000)+200)){
                outputText.setText(outputText.getText()+"•");
                lettre += "c";
            }else if(delta>((tempo*4000)-200) & delta<((tempo*4000)+200)){
                outputText.setText(outputText.getText()+"•");
                lettre += "c";
                decodeLettre(lettre);
                lettre = "";
            }else if(delta>((tempo*8000)-200) & delta<((tempo*8000)+200)){
                outputText.setText(outputText.getText()+"• ~");
                lettre += "c";
                decodeLettre(lettre);
                textReponse.setText(textReponse.getText()+" ");
                lettre = "";
            }
        }
    }

    public void decodeLettre(String lettre){

        switch(lettre){
            case "cl":
                textReponse.setText(textReponse.getText()+"A");
                break;
            case "lccc":
                textReponse.setText(textReponse.getText()+"B");
                break;
            case "lclc":
                textReponse.setText(textReponse.getText()+"C");
                break;
            case "lcc":
                textReponse.setText(textReponse.getText()+"D");
                break;
            case "c":
                textReponse.setText(textReponse.getText()+"E");
                break;
            case "cclc":
                textReponse.setText(textReponse.getText()+"F");
                break;
            case "llc":
                textReponse.setText(textReponse.getText()+"G");
                break;
            case "cccc":
                textReponse.setText(textReponse.getText()+"H");
                break;
            case "cc":
                textReponse.setText(textReponse.getText()+"I");
                break;
            case "clll":
                textReponse.setText(textReponse.getText()+"J");
                break;
            case "lcl":
                textReponse.setText(textReponse.getText()+"K");
                break;
            case "clcc":
                textReponse.setText(textReponse.getText()+"L");
                break;
            case "ll":
                textReponse.setText(textReponse.getText()+"M");
                break;
            case "lc":
                textReponse.setText(textReponse.getText()+"N");
                break;
            case "lll":
                textReponse.setText(textReponse.getText()+"O");
                break;
            case "cllc":
                textReponse.setText(textReponse.getText()+"p");
                break;
            case "llcl":
                textReponse.setText(textReponse.getText()+"Q");
                break;
            case "clc":
                textReponse.setText(textReponse.getText()+"R");
                break;
            case "ccc":
                textReponse.setText(textReponse.getText()+"S");
                break;
            case "l":
                textReponse.setText(textReponse.getText()+"T");
                break;
            case "ccl":
                textReponse.setText(textReponse.getText()+"U");
                break;
            case "cccl":
                textReponse.setText(textReponse.getText()+"V");
                break;
            case "cll":
                textReponse.setText(textReponse.getText()+"W");
                break;
            case "lccl":
                textReponse.setText(textReponse.getText()+"X");
                break;
            case "lcll":
                textReponse.setText(textReponse.getText()+"Y");
                break;
            case "llcc":
                textReponse.setText(textReponse.getText()+"Z");
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
                textReponse.setText(" ");
                triple = false;
                tap1 = null;
                tap2 = null;
            }
        }, 5000);
        lettre = "";
    }

    public void stopPhrase(View view){
        if(triple){
            outputText.setText(outputText.getText()+"~");
            decodeLettre(lettre);
            textReponse.setText(textReponse.getText()+" ");
            lettre = "";
        }else{
            outputText.setText(outputText.getText()+"• ~");
            lettre += "c";
            decodeLettre(lettre);
            textReponse.setText(textReponse.getText()+" ");
            lettre = "";
        }
        triple = false;
        tap1 = null;
        tap2 = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void infoAct (View view){
        startActivity(new Intent(this, InfoActivity.class));
    }
}
