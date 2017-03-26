package com.egyed.adam.laststandvr_controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GyroTestActivity extends AppCompatActivity {

    private SensorManager sensorManager;

    private GyroListener gyroListener;

    private double xRotOffset = 0;
    private double yRotOffset = 0;
    private double zRotOffset = 0;

    boolean calibrationRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_test);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroListener = new GyroListener();
    }

    public void calibrate(View view) {
        calibrationRequested = true;
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
                TextView displayX = (TextView) findViewById(R.id.sensorDataViewX);
                TextView displayY = (TextView) findViewById(R.id.sensorDataViewY);
                TextView displayZ = (TextView) findViewById(R.id.sensorDataViewZ);

                if (calibrationRequested) {
                    xRotOffset = event.values[0];
                    yRotOffset = event.values[1];
                    zRotOffset = event.values[2];
                    calibrationRequested = false;
                }

                double xRot = event.values[0] - xRotOffset;
                double yRot = event.values[1] - yRotOffset;
                double zRot = event.values[2] - zRotOffset;

                String x = String.format("%4f",xRot);
                String y = String.format("%4f",yRot);
                String z = String.format("%4f",zRot);


                displayX.setText("X:"+x);
                displayY.setText("Y:"+y);
                displayZ.setText("Z:"+z);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
