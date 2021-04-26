package com.example.demo;

import com.example.demo.model.Report;
import com.example.demo.service.ReportService;
import com.example.demo.service.ReportServiceImpl;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<Integer, String> redisIdUrl = new HashMap<>();
    private static final ReportService reportService = new ReportServiceImpl();

    static {
        redisIdUrl.put(1, "localhost");
        redisIdUrl.put(2, "localhost");
        redisIdUrl.put(3, "localhost");
    }

    public static void main(String[] args) {
        try {
            System.out.println(new Date() + " Run");
            Connection natsConnection = Nats.connect();
            Dispatcher dispatcher = natsConnection.createDispatcher((msg) -> {
            });

            dispatcher.subscribe("events", (msg) -> {
                System.out.println(new Date() + " Subscribe to vowel-cons-rater-jobs");
                String response = new String(msg.getData(), StandardCharsets.UTF_8);
                final int i = response.indexOf(" ");
                final String type = response.substring(0, i);
                if (type.equals("TextRankCalculated")) {
                    final int endIndex = response.indexOf(" ", i + 1);
                    final String jobId = response.substring(i + 1, endIndex);
                    final int i1 = response.indexOf(' ', endIndex + 1);
                    final float textRank = Float.parseFloat(response.substring(endIndex + 1, i1));
                    final int redisServerId = Integer.parseInt(response.substring(i1 + 1));
                    final String redisUrl = redisIdUrl.get(redisServerId);
                    if (redisUrl == null) {
                        System.out.println(new Date() + " Invalid redis server id " + redisServerId);
                    } else {
                        System.out.println(new Date() + " jobId: " + jobId + ", redisServerId: " + redisServerId);
                        final Report report1 = reportService.updateReport(textRank);
                        final String report = report1.toString();
                        System.out.println(report);
                        System.out.println(new Date() + " " + jobId + ", " + report);
                    }
                } else if (type.equals("ProcessingAccepted")) {
                    final int i1 = response.indexOf(" ", i + 1);
                    final String substring = response.substring(i1 + 1);
                    final boolean isAccepted = Boolean.parseBoolean(substring);
                    if (!isAccepted) {
                         reportService.incrementCountOfBlockedRequest();
                    }
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
