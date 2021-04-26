package com.example.demo.service;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Override
    public String getReport() {
        final Jedis jedis = new Jedis("localhost");
        final String report = jedis.get("report");
        if (report == null) {

            return "{\"textNumber\": 0, \"highRankPart\": 0, \"averageRank\": 0, \"blockedRequest\": 0}";
        }

        return report;
    }
}