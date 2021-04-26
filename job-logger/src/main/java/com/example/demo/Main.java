package com.example.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<Integer, String> redisIdUrl = new HashMap<>();

    static {
        redisIdUrl.put(1, "localhost");
        redisIdUrl.put(2, "localhost");
        redisIdUrl.put(3, "localhost");
    }

    public static void main(String[] args) {
        try {
            System.out.println("Run");
            Connection natsConnection = Nats.connect();
            Dispatcher d = natsConnection.createDispatcher((msg) -> {
            });

            Subscription s = d.subscribe("events", (msg) -> {
                System.out.println("Event");
                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                System.out.println(response);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                if (type.equals("JobCreated")) {
                    final int i1 = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i+ 1, i1);
                    final Integer redisServerId = Integer.valueOf(response.substring(i1 + 1));
                    System.out.println(jobId + "'redisServerId' " + redisServerId  + "'");
                    final String redisUrl = redisIdUrl.get(redisServerId);
                    if (redisUrl == null) {
                        System.out.println("Invalid redis server id " + redisServerId);
                    } else {
                        Jedis jedis = new Jedis(redisUrl);
                        final String jobDescription = jedis.get(jobId);
                        System.out.println(new Date() + " " + redisServerId + " " + jobId + ", " + jobDescription);
                    }
                }
            });

            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
