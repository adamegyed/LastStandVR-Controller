package com.egyed.adam.laststandvr_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
}
