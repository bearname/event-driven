package com.example.demo.model;

public class Report {
    private int textNumber = 0;
    private int highRankPart = 0;
    private float averageRank = 0;
    private int blockedRequest = 0;

    public Report() {
    }

    public Report(int textNumber, int highRankPart, float averageRank) {
        this.textNumber = textNumber;
        this.highRankPart = highRankPart;
        this.averageRank = averageRank;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }

    public void setHighRankPart(int highRankPart) {
        this.highRankPart = highRankPart;
    }

    public void setAverageRank(float averageRank) {
        this.averageRank = averageRank;
    }

    public int getTextNumber() {
        return textNumber;
    }

    public int getHighRankPart() {
        return highRankPart;
    }

    public float getAverageRank() {
        return averageRank;
    }

    public int incrementBlockedRequest() {
        this.blockedRequest++;
        return this.blockedRequest;
    }

    @Override
    public String toString() {
        return "{\"textNumber\":" + textNumber +
                ", \"highRankPart\":" + highRankPart +
                ", \"averageRank\":" + averageRank +
                ", \"blockedRequest\":" + blockedRequest +
                '}';
    }
}
