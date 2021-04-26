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

    static  {
        redisIdUrl.put(1, "localhost");
        redisIdUrl.put(2, "localhost");
        redisIdUrl.put(3, "localhost");
    }

    public static void main(String[] args) {
        try {
            System.out.println(new Date() + " Run");

            Connection natsConnection = Nats.connect();
            Dispatcher d = natsConnection.createDispatcher((msg) -> {
            });

            Subscription s = d.subscribe("vowel-cons-counter-jobs", (msg) -> {
                System.out.println(new Date() + " Subscribe to vowel-cons-counter-jobs");

                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                System.out.println(response);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                System.out.println(type + "'");
                if (type.equals("CalculateVowelConsJob")) {
                    System.out.println("CalculateVowelConsJob");
                    final int i1 = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i + 1, i1);
                    final String substring = response.substring(i1 + 1);
                    final int redisServerId = Integer.parseInt(substring);
                    final String redisUrl = redisIdUrl.get(redisServerId);
                    if (redisUrl == null) {
                        System.out.println(new Date() + " Invalid redis server id " + redisServerId);
                    } else {
                        System.out.println(jobId + " " + redisServerId);
                        Jedis jedis = new Jedis(redisUrl);
                        final String jobDescription = jedis.get(jobId);
                        System.out.println(jobDescription);

                        int countVowels = 0;
                        int countConsonants = 0;
                        for (char ch : jobDescription.toCharArray()) {
                            if ("AEIOUYaeiouy".contains(String.valueOf(ch))) {
                                countVowels++;
                            } else if ("BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz".contains(String.valueOf(ch))) {
                                countConsonants++;
                            }
                        }
                        System.out.println(countVowels + " " + countConsonants);
                        final String publishData = "RateVowelConsJob " + jobId + " " + countVowels + " " + countConsonants + " " + redisServerId;
                        System.out.println(new Date() + " Publish '" + publishData + "'");
                        natsConnection.publish("vowel-cons-rater-jobs", publishData.getBytes(StandardCharsets.UTF_8));
                        System.out.println(new Date() + " " + jobId);
                    }
                }
            });

            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
