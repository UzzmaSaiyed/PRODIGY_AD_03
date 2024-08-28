package com.example.stopwatch;
public class LapItem {
    private int lapNumber;
    private String lapTime;
    private String overallTime;

    public LapItem(int lapNumber, String lapTime, String overallTime) {
        this.lapNumber = lapNumber;
        this.lapTime = lapTime;
        this.overallTime = overallTime;
    }

    public int getLapNumber() {
        return lapNumber;
    }

    public String getLapTime() {
        return lapTime;
    }

    public String getOverallTime() {
        return overallTime;
    }
}
