package com.cambio.finalprojectandroid.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Model {
    public final static Model instace = new Model();
    private String studentId;



    private Model(){
        for(int i=0;i<20;i++){

            Event ev = new Event();
            ev.setName("Student " + i);
            ev.setId("" + i);
            ev.setImageUrl("");
            ev.setYear(1991);
            ev.setMonth(11);
            data.add(ev);
        }
    }

    public Model(List<Event> data) {
        this.data = data;
    }



    private List<Event> data = new LinkedList<Event>();

    public List<Event> getAllEvents(){
        return data;
    }

    public void addEventt(Event st){
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
