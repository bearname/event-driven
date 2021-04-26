package com.example.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Subscription;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Main {
    public static final int MAX_FREE_REQUEST = 3;
    private static int numberOfFreeRequest = MAX_FREE_REQUEST;

    public static void main(String[] args) {
        try {
            System.out.println(new Date() + "Run");
            Connection natsConnection = Nats.connect();
            Dispatcher dispatcher = natsConnection.createDispatcher((msg) -> {
            });

            final Subscription subscribe = dispatcher.subscribe("events", (msg) -> {
                System.out.println("is limited" + isLimited());
                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                if (type.equals("JobCreated")) {
                    System.out.println(new Date() + "Event" + response);
                    final int i1 = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i + 1, i1);

                    boolean status = !isLimited();

                    publish(natsConnection, jobId, status);
                    System.out.println(new Date() + " " + jobId);
                } else if (type.equals("TextRankCalculated")) {
                    final int endIndex = response.indexOf(" ", i + 1);
                    final int i1 = response.indexOf(' ', endIndex + 1);
                    final float textRank = Float.parseFloat(response.substring(endIndex + 1, i1));

                    if (textRank < 0.5) {
                        numberOfFreeRequest = MAX_FREE_REQUEST;
                    } else {
                        if (numberOfFreeRequest > 0) {
                            numberOfFreeRequest--;
                        }
                    }
                }
            });
            System.out.println();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static boolean isLimited() {
        return numberOfFreeRequest == 0;
    }

    private static void publish(Connection natsConnection, String jobId, final boolean status) {
        natsConnection.publish("events", ("ProcessingAccepted " + jobId + " " + status).getBytes(StandardCharsets.UTF_8));
    }
}
