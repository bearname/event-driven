package com.example.demo.config;

import com.example.demo.model.RegionCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class RedisConfig {
    public static final Map<RegionCode, Integer> redisServerId = new HashMap<>();
    public static final Map<Integer, String> redisIdUrl = new HashMap<>();
    static {
        redisServerId.put(RegionCode.RUS, 1);
        redisServerId.put(RegionCode.EU, 2);
        redisServerId.put(RegionCode.USA, 3);
        redisIdUrl.put(1, "localhost");
        redisIdUrl.put(2, "localhost");
        redisIdUrl.put(3, "localhost");
    }
}
