package com.cambio.finalprojectandroid.utils;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Date {

    private int year;
    private int month;
    private int dayOfManth;

    public Date(int year, int month, int dayOfManth) {
        this.year = year;
        this.month = month;
        this.dayOfManth = dayOfManth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfManth() {
        return dayOfManth;
    }

    public void setDayOfManth(int dayOfManth) {
        this.dayOfManth = dayOfManth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        if (year != date.year) return false;
        if (month != date.month) return false;
        return dayOfManth == date.dayOfManth;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + dayOfManth;
        return result;
    }

    @Override
    public String toString() {
        return "" +
                 year +
                "/" + month +
                "/" + dayOfManth
                ;
    }
}
