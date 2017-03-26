package com.egyed.adam.laststandvr_controller;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import redis.clients.jedis.exceptions.JedisException;

public class RedisTestActivity extends AppCompatActivity {
    private static final String TAG = "RedisTestActivity";

    boolean connected = false;

    JedisManager jm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redis_test);

        jm = new JedisManager();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    public void connect(View view) {
        try {
            EditText ipEditText = (EditText)findViewById(R.id.ipEditText);
            String ip = ipEditText.getText().toString();
            EditText portEditText = (EditText)findViewById(R.id.portEditText);
            int port = Integer.valueOf(portEditText.getText().toString());
            jm.connect(ip, port);
            Toast toast = Toast.makeText(getApplicationContext(), "Succesfully connected", Toast.LENGTH_SHORT);
            toast.show();
            connected = true;
        } catch (JedisException | NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT);
            toast.show();
            connected = false;
        }



    }

    public void ping(View view) {
        if (connected) {
            try {
                String ping = jm.ping();
                Toast toast = Toast.makeText(getApplicationContext(), ping, Toast.LENGTH_SHORT);
                toast.show();
            } catch (JedisException e) {
                Log.e(TAG,"Exception caught: \n"+e.getMessage());
            }
        }
    }
}
