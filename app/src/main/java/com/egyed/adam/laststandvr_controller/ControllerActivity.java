package com.egyed.adam.laststandvr_controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ControllerActivity extends AppCompatActivity {
    public static final String TAG = "ControllerActivity";

    private SensorManager sensorManager;

    private GyroListener gyroListener;

    private double xRotOffset = 0;
    private double yRotOffset = 0;
    private double zRotOffset = 0;

    boolean calibrationRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroListener = new GyroListener();
    }

    public void calibrate(View view) {
        Log.i(TAG,"Calibrate method in correct activity");
        calibrationRequested = true;
    }

    public void fire(View view) {
        // TODO: fire over redis
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroListener.stop();
    }

    class GyroListener implements SensorEventListener {

        private Sensor rotationVectorSensor;

        public GyroListener() {
            rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        public void start() {
            sensorManager.registerListener(this, rotationVectorSensor, 10000);
        }

        public void stop() {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                if (calibrationRequested) {
                    xRotOffset = event.values[0];
                    yRotOffset = event.values[1];
                    zRotOffset = event.values[2];
                    calibrationRequested = false;
                }

                // TODO: Send data over network or something
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
