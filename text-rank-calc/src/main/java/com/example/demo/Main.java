package com.example.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Subscription;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Run");
            Connection natsConnection = Nats.connect();
            Dispatcher dispatcher = natsConnection.createDispatcher((msg) -> {
            });

            final Subscription subscription = dispatcher.subscribe("events", (msg) -> {
                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                if (type.equals("ProcessingAccepted")) {
                    System.out.println("Event" + response);
                    final int i1 = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i + 1, i1);

                    final String substring = response.substring(i1 + 1);
                    final boolean isAccepted = Boolean.parseBoolean(substring);

                    if (isAccepted) {
                        //System.out.println(jobId + "' redisServerId " + redisServerId  + "'");
                        final String s = "CalculateVowelConsJob " + jobId + " " + 1;
                        System.out.println("publish" + s);
                        natsConnection.publish("vowel-cons-counter-jobs", s.getBytes(StandardCharsets.UTF_8));

                        System.out.println(new Date() + " " + jobId);
                    } else {
                        new Jedis("localhost").set(jobId + "calc", String.valueOf(-1));
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
