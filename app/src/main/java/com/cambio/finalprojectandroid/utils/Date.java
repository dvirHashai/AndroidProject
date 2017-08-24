package com.cambio.finalprojectandroid.utils;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Date {

    private int year;
    private int month;
    private int dayOfMonth;

    public Date(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public Date(Date date){
        this.year = date.getYear();
        this.month = date.getMonth();
        this.dayOfMonth = date.getDayOfMonth();
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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date = (Date) o;

        if (year != date.year) return false;
        if (month != date.month) return false;
        return dayOfMonth == date.dayOfMonth;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + dayOfMonth;
        return result;
    }

    @Override
    public String toString() {
        return "" +
                 dayOfMonth +
                "/" + month +
                "/" + year
                ;
    }
}
