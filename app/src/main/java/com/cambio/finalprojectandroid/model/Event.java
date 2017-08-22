package com.cambio.finalprojectandroid.model;

import java.io.Serializable;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Event implements Serializable {

    private String id;
    private String name;
    private int year;
    private int month;
    private int dayOfManth;
    private int price;
    private String location;
    private String imageUrl;
    private String lastUpDateTime;

    public Event(String id, String name, int year, int month, int dayOfManth, int price, String location, String imageUrl, String lastUpDateTime) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.month = month;
        this.dayOfManth = dayOfManth;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
        this.lastUpDateTime = lastUpDateTime;
    }

    public Event() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastUpDateTime() {
        return lastUpDateTime;
    }

    public void setLastUpDateTime(String lastUpDateTime) {
        this.lastUpDateTime = lastUpDateTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", dayOfManth=" + dayOfManth +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", lastUpDateTime='" + lastUpDateTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (year != event.year) return false;
        if (month != event.month) return false;
        if (dayOfManth != event.dayOfManth) return false;
        if (price != event.price) return false;
        if (!id.equals(event.id)) return false;
        if (!name.equals(event.name)) return false;
        if (!location.equals(event.location)) return false;
        if (!imageUrl.equals(event.imageUrl)) return false;
        return lastUpDateTime.equals(event.lastUpDateTime);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + dayOfManth;
        result = 31 * result + price;
        result = 31 * result + location.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + lastUpDateTime.hashCode();
        return result;
    }
}
