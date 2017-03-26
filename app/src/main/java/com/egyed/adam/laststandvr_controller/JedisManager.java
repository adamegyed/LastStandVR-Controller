package com.egyed.adam.laststandvr_controller;

import android.util.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by rithikjain on 3/25/17.
 */
public class JedisManager {
    public static String TAG = "JedisManager";

    private boolean connected = false;

    private Jedis jedis;

    public JedisManager(){

    }

    public void connect(String ip, int port) {
        jedis = new Jedis(ip, port);
        try {
            jedis.ping();
        } catch (JedisException e) {
            return;
        }
        connected = true;
        jedis.lpush("appPlatform", "Android");
    }


    public void sendRotation(double xr, double yr, double zr) {
        if (!connected) {
            Log.e(TAG, "Not connected");
        }
        jedis.lset("xRot", 0, Double.toString(xr));
        jedis.lset("yRot", 0, Double.toString(yr));
        jedis.lset("zRot", 0, Double.toString(zr));
    }

    public void sendFire(){
        if (!connected) {
            Log.e(TAG, "Not connected");
        }
        jedis.lset("fire", 0, "1");
    }

    public String ping() {
        return jedis.ping();
    }
}
