package com.cambio.finalprojectandroid.model;

import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;

import java.io.Serializable;


/**
 * Created by dvirh on 8/22/2017.
 */

public class Event implements Serializable {

    private String id;
    private String name;
    private Date date;
    private Time time;
    private String price;
    private String location;
    private String imageUrl;
    private double lastUpDateTime;



    public Event(String id, String name, Date date, Time time, String price, String location, String imageUrl, double lastUpDateTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
        this.lastUpDateTime = lastUpDateTime;
    }

    public Event(Event event){
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
        this.time = event.getTime();
        this.price = event.getPrice();
        this.location = event.getLocation();
        this.imageUrl = event.getImageUrl();
        this.lastUpDateTime = event.getLastUpDateTime();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public double getLastUpDateTime() {
        return lastUpDateTime;
    }

    public void setLastUpDateTime(double lastUpDateTime) {
        this.lastUpDateTime = lastUpDateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (Double.compare(event.lastUpDateTime, lastUpDateTime) != 0) return false;
        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (date != null ? !date.equals(event.date) : event.date != null) return false;
        if (time != null ? !time.equals(event.time) : event.time != null) return false;
        if (price != null ? !price.equals(event.price) : event.price != null) return false;
        if (location != null ? !location.equals(event.location) : event.location != null)
            return false;
        return imageUrl != null ? imageUrl.equals(event.imageUrl) : event.imageUrl == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        temp = Double.doubleToLongBits(lastUpDateTime);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
