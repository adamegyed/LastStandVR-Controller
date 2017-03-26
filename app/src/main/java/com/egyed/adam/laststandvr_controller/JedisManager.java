package com.egyed.adam.laststandvr_controller;

import redis.clients.jedis.Jedis;

/**
 * Created by rithikjain on 3/25/17.
 */
public class JedisManager {
    public static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public JedisManager(){
        jedis.lpush("list", "0");
        jedis.lset("list", 0, "0");
    }
}
