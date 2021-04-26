package com.example.demo.service;

import com.example.demo.model.Report;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class ReportServiceImpl implements ReportService {
    private Report report;
    private Jedis jedis;

    public ReportServiceImpl() {
        setJedis(new Jedis("localhost"));
    }

    public ReportServiceImpl(Jedis jedis) {
        setJedis(jedis);
    }

    private void initReport() {
        final String report = this.jedis.get("report");
        if (report != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(report);
            this.report = gson.fromJson(object, Report.class);
        } else {
            calculateFirst();
        }
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
        initReport();
    }

    @Override
    public Report updateReport(float textRank) {
        if (this.report == null) {
            calculateFirst();
        } else {
            recalculateReport(textRank);
        }

        return report;
    }

    @Override
    public int incrementCountOfBlockedRequest() {
        return report.incrementBlockedRequest();
    }

    private void calculateFirst() {
        final Set<String> keys = jedis.keys("*");
        int textNumber = 0;
        int highRankPart = 0;
        double sumRank = 0;
        for (String key : keys) {
            if (key.length() == 40) {
                final float rank = Float.parseFloat(jedis.get(key));
                sumRank += rank;
                textNumber++;
                if (rank > 5) {
                    highRankPart++;
                }
            }
        }

        if (report == null) {
            this.report = new Report(textNumber, highRankPart, (float) (sumRank / textNumber));
        } else {
            report.setHighRankPart(highRankPart);
            report.setTextNumber(textNumber);
            report.setAverageRank((float) (sumRank / textNumber));
        }
    }

    private void recalculateReport(float textRank) {
        this.report.setTextNumber(this.report.getTextNumber() + 1);

        final float oldSum = report.getAverageRank() * report.getTextNumber();
        report.setAverageRank((oldSum + textRank) / report.getTextNumber());
        if (textRank > 5) {
            report.setHighRankPart(report.getHighRankPart() + 1);
        }

        jedis.set("report", report.toString());
    }
}