package com.example.demo.service;

import com.example.demo.config.RedisConfig;
import com.example.demo.model.Job;
import com.example.demo.model.RegionCode;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {
    private Jedis jedis;
    private final Connection natsConnection;

    public JobServiceImpl() throws IOException, InterruptedException {
        jedis = new Jedis("localhost");
        natsConnection = Nats.connect();
    }

    @Override
    public String register(Job job) {
        final String id = UUID.randomUUID().toString();
        job.setId(id);
        jedis.set(id, job.getDescription());
        natsConnection.publish("events", ("JobCreated " + id).getBytes(StandardCharsets.UTF_8));
        return id;
    }

    @Override
    public String register(Job job, RegionCode regionCode) {
        final Integer redisServerId = RedisConfig.redisServerId.get(regionCode);
        final String id = UUID.randomUUID().toString();
        job.setId(id);

        final String host = RedisConfig.redisIdUrl.get(redisServerId);
        jedis = new Jedis(host);
        jedis.set(id, job.getDescription());

        final String s = "JobCreated " + id + " " + redisServerId;
        System.out.println("devent" + s);
        natsConnection.publish("events", s.getBytes(StandardCharsets.UTF_8));
        return id;
    }

    @Override
    public Float getProcessingResult(final String jobId) {
        final String s = jedis.get(jobId + "calc");
        if (s == null) {
            return null;
        }
        return Float.parseFloat(s);
    }
}