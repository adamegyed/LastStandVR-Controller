package com.egyed.adam.laststandvr_controller;

import android.util.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
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

    public void connect(String ip, int port) throws JedisException {
        jedis = new Jedis(ip, port, 5000);
        jedis.ping();
        connected = true;
        jedis.set("appPlatform", "Android");
        jedis.set("xRot", "0");
        jedis.set("yRot", "0");
        jedis.set("zRot", "0");
    }


    public void sendRotation(double xr, double yr, double zr) {
        if (!connected) {
            Log.e(TAG, "Not connected");
            return;
        }
        try {
            jedis.set("xRot", Double.toString(xr));
            jedis.set("yRot", Double.toString(yr));
            jedis.set("zRot", Double.toString(zr));
        } catch (JedisConnectionException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    public void sendFire(){
        if (!connected) {
            Log.e(TAG, "Not connected");
            return;
        }
        jedis.lset("fire", 0, "1");
    }

    public String ping() {
        return jedis.ping();
    }

    public boolean isConnected() {
        return connected;
    }
}
