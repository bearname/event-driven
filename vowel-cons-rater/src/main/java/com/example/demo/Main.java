package com.example.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
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
        System.out.println(new Date() + "Run");
        try {
            Connection natsConnection = Nats.connect();

            Dispatcher d = natsConnection.createDispatcher((msg) -> {
            });

            d.subscribe("vowel-cons-rater-jobs", (msg) -> {
                System.out.println(new Date() + " Subscribe to vowel-cons-rater-jobs");
                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                if (type.equals("RateVowelConsJob")) {
                    final int endIndex = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i + 1, endIndex);
                    final int i1 = response.indexOf(' ', endIndex + 1);
                    int countVowels = Integer.parseInt(response.substring(endIndex + 1, i1));
                    final int i2 = response.indexOf(" ", i1 + 1);
                    int countConsonants = Integer.parseInt(response.substring(i1 + 1, i2));
                    final int redisServerId = Integer.parseInt(response.substring(i2 + 1));
                    final String redisUrl = redisIdUrl.get(redisServerId);
                    if (redisUrl == null) {
                        System.out.println(new Date() + "Invalid redis server id " + redisServerId);
                    } else {
                        System.out.println(new Date() + " jobId: " + jobId + ", redisServerId: " + redisServerId);
                        final String rank;
                        if (countConsonants == 0) {
                            rank = String.valueOf(countVowels);
                        } else {
                            rank = String.valueOf((float) (countVowels / countConsonants));
                        }

                        System.out.println(countVowels + " " + countConsonants + " " + rank);
                        Jedis jedis = new Jedis(redisUrl);

                        jedis.set(jobId + "calc", rank);
                        System.out.println(new Date() + " " + jobId + ", " + rank);
                        natsConnection.publish("events", ("TextRankCalculated " + jobId + " " + rank + " " + redisServerId).getBytes(StandardCharsets.UTF_8));
                    }
                }
            });

            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
