package com.egyed.adam.laststandvr_controller;

import redis.clients.jedis.Jedis;

/**
 * Created by rithikjain on 3/25/17.
 */
public class JedisManager {

    private boolean connected = false;

    private Jedis jedis;

    public JedisManager(){

    }

    public void connect(String ip, int port) {
        jedis = new Jedis(ip, port);
    }


    public void test() {
        jedis.lpush("list", "0");
        jedis.lset("list", 0, "0");
    }

    public String ping() {
        return jedis.ping();
    }
}
