package com.egyed.adam.laststandvr_controller;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import redis.clients.jedis.exceptions.JedisException;

public class ControllerActivity extends AppCompatActivity {
    public static final String TAG = "ControllerActivity";

    public static final int MS_PER_UPDATE = 250;

    private JedisManager jm;

    private SensorManager sensorManager;

    private GyroListener gyroListener;

    private double xRotOffset = 0;
    private double yRotOffset = 0;
    private double zRotOffset = 0;

    private final double[] rotValues = new double[3];

    boolean calibrationRequested = false;

    boolean sendingData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroListener = new GyroListener();

        jm = new JedisManager();

        Intent i = getIntent();

        try {
            jm.connect(i.getStringExtra("IP"), Integer.valueOf(i.getStringExtra("Port")));
        } catch (JedisException | NumberFormatException e) {
            Log.e(TAG, e.getMessage());
            finish();
            return;
        }
        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();


    }

    public void calibrate(View view) {
        Log.i(TAG,"Calibrate method in correct activity");
        calibrationRequested = true;
    }

    public void fire(View view) {

        class FireTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                jm.sendFire();

                return null;
            }
        }

        new FireTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroListener.start();
        sendingData = true;
        startSubmitLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroListener.stop();
        sendingData = false;
    }

    class GyroListener implements SensorEventListener {

        private Sensor rotationVectorSensor;

        public GyroListener() {
            rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        public void start() {
            sensorManager.registerListener(this, rotationVectorSensor, 4_000_000);
        }

        public void stop() {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                final SensorEvent event = sensorEvent;
                if (calibrationRequested) {


                    xRotOffset = event.values[0];
                    yRotOffset = event.values[1];
                    zRotOffset = event.values[2];
                    Toast.makeText(getApplicationContext(), "Calibrated", Toast.LENGTH_SHORT).show();
                    calibrationRequested = false;
                }

                rotValues[0] = event.values[0] - xRotOffset;
                rotValues[1] = event.values[1] - yRotOffset;
                rotValues[2] = event.values[2] - zRotOffset;


            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    private void sendDataNotUsed(double[] values) {
        double[] rotations = new double[3];
        final int[] results = new int[3];

        rotations[0] = values[0] - xRotOffset;
        rotations[1] = values[1] - yRotOffset;
        rotations[2] = values[2] - zRotOffset;

        for (int i = 0; i < 3; i++) {
            if (rotations[i] > 0) {
                if (rotations[i] > 0.9) results[i] = 5;
                else if (rotations[i] > 0.7) results[i] = 4;
                else if (rotations[i] > 0.5) results[i] = 3;
                else if (rotations[i] > 0.3) results[i] = 2;
                else if (rotations[i] > 0.1) results[i] = 1;
                else results[i] = 0;
            } else {
                if (rotations[i] < -0.9) results[i] = -5;
                else if (rotations[i] < -0.7) results[i] = -4;
                else if (rotations[i] < -0.5) results[i] = -3;
                else if (rotations[i] < -0.3) results[i] = -2;
                else if (rotations[i] < -0.1) results[i] = -1;
                else results[i] = 0;
            }
        }


    }

    private void startSubmitLoop() {

        class RotDataTask extends AsyncTask<Void, Void, Void> {


            @Override
            protected Void doInBackground(Void... voids) {
                jm.sendRotation(rotValues[0], rotValues[1], rotValues[2]);

                return null;
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new RotDataTask().execute();
                if (sendingData) startSubmitLoop();
            }
        }, MS_PER_UPDATE);
    }
}
