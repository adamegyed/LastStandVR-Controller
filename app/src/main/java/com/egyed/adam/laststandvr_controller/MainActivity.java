package com.egyed.adam.laststandvr_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import redis.clients.jedis.Jedis;

public class MainActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread thread = new Thread();
        //thread.start();
    }

    public void run() {
        JedisManager jedisManager = new JedisManager();
    }

    public void startGyroTest(View view) {
        Intent intent = new Intent(this, GyroTestActivity.class);
        startActivity(intent);
    }

    public void startRedisTest(View view) {
        Intent intent = new Intent(this, RedisTestActivity.class);
        startActivity(intent);
    }

    public void startController(View view) {
        Intent intent = new Intent(this, ControllerActivity.class);
        EditText ipEditText = (EditText) findViewById(R.id.ipEditText);
        String ip = ipEditText.getText().toString();
        intent.putExtra("IP", ip);

        EditText portEditText = (EditText) findViewById(R.id.portEditText);
        String port = portEditText.getText().toString();
        intent.putExtra("Port", port);


        startActivity(intent);
    }
}
