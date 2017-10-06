package com.brianlukonsolo.beans;

import java.time.LocalDate;
import java.time.LocalTime;

public class ForexPriceRecord {
    private LocalDate date;
    private LocalTime time;
    private float open, high, low, close;
    private int volume;

    public ForexPriceRecord(LocalDate date, LocalTime time, float open, float high, float low, float close, int volume) {
        this.date = date;
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;


    }

    @Override
    public String toString() {
        final String COMMA = ", ";
        String record = "#[DAY]> " + date.getDayOfWeek() + COMMA +
                "[DATE]> " + this.date + COMMA +
                "#[TIME]> " + this.time + COMMA +
                "#[OPEN]> " + this.open + COMMA +
                "#[HIGH]> " + this.high + COMMA +
                "#[LOW]> " + this.low + COMMA +
                "#[CLOSE]> " + this.close + COMMA +
                "#[VOLUME]> " + this.volume;

        return record;


    }

    public float getHighLowDifference(){
        return high - low;
    }

    public float getOpenCloseDifference(){
        return open - close;
    }

    public float getOpenHighDifference(){
        return open - high;
    }

    public float getCloseLowDifference(){
        return close - low;
    }

    public  float getCloseHightDifference(){
        return close - high;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public float getOpen() {
        return open;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getClose() {
        return close;
    }

    public float getVolume() {
        return volume;
    }

}
