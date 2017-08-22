package com.cambio.finalprojectandroid.utils;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Time {

    private int hour;
    private int minute;


    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(Time time){
        this.hour = time.getHour();
        this.minute = time.getMinute();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        if (hour != time.hour) return false;
        return minute == time.minute;

    }

    @Override
    public int hashCode() {
        int result = hour;
        result = 31 * result + minute;
        return result;
    }

    @Override
    public String toString() {
        return "" + hour +
                ":" + minute;
    }
}
