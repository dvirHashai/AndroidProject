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
    private String lastUpDateTime;



    public Event(String id, String name, Date date, Time time, String price, String location, String imageUrl, String lastUpDateTime) {
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

    public String getLastUpDateTime() {
        return lastUpDateTime;
    }

    public void setLastUpDateTime(String lastUpDateTime) {
        this.lastUpDateTime = lastUpDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!id.equals(event.id)) return false;
        if (!name.equals(event.name)) return false;
        if (!date.equals(event.date)) return false;
        if (!time.equals(event.time)) return false;
        if (!price.equals(event.price)) return false;
        if (!location.equals(event.location)) return false;
        if (!imageUrl.equals(event.imageUrl)) return false;
        return lastUpDateTime.equals(event.lastUpDateTime);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + lastUpDateTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", price='" + price + '\'' +
                ", location='" + location + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", lastUpDateTime='" + lastUpDateTime + '\'' +
                '}';
    }
}
