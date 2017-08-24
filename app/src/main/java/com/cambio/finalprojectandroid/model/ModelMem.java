package com.cambio.finalprojectandroid.model;

import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dvirh on 8/24/2017.
 */

public class ModelMem {

    private String studentId;



     ModelMem(){
        for(int i=0;i<20;i++){

            Event event = new Event();
            event.setName("Student " + i);
            event.setId("" + i);
            event.setPrice("100");
            event.setImageUrl("");
            event.setDate( new Date(1,11,1991));
            event.setTime(new Time(1,1));
            event.setLocation("");

            data.add(event);
        }
    }

    private List<Event> data = new LinkedList<Event>();

    public List<Event> getAllEvents(){
        return data;
    }

    public void addEvent(Event st){
        data.add(st);
    }

    public void removeEvent(Event st){
        int index = data.indexOf(st);
        data.remove(index);
    }


    public Event getEvent(String stId) {
        for (Event s : data){
            if (s.getId().equals(stId)){
                return s;
            }
        }
        return null;
    }
    public String getEventId() {
        return studentId;
    }

    public void setEventId(String studentId) {
        this.studentId = studentId;
    }
}
