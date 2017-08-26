package com.cambio.finalprojectandroid.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dvirh on 8/24/2017.
 */

public class EventSql {

    static final String EVENT_TABLE = "events";
    static final String EVENT_ID = "id";
    static final String EVENT_NAME = "name";
    static final String EVENT_DATE = "date";
    static final String EVENT_TIME = "time";
    static final String EVENT_PRICE = "price";
    static final String EVENT_LOCATION = "location";
    static final String EVENT_IMAGE_URL = "imageUrl";
    static final String EVENT_LAST_UPDATE = "lastUpdateDate";

    static List<Event> getAllEvents(SQLiteDatabase db) {
        Cursor cursor = db.query("events", null, null, null, null, null, null);
        List<Event> list = new LinkedList<Event>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(EVENT_ID);
            int nameIndex = cursor.getColumnIndex(EVENT_NAME);
            int dateIndex = cursor.getColumnIndex(EVENT_DATE);
            int timeIndex = cursor.getColumnIndex(EVENT_TIME);
            int priceIndex = cursor.getColumnIndex(EVENT_PRICE);
            int locationIndex = cursor.getColumnIndex(EVENT_LOCATION);
            int imageUrlIndex = cursor.getColumnIndex(EVENT_IMAGE_URL);
            int lastUpdateDateIndex = cursor.getColumnIndex(EVENT_LAST_UPDATE);

            do {
                Event event = new Event();
                event.setId(cursor.getString(idIndex));
                event.setName(cursor.getString(nameIndex));
                event.setDate(event.getDate().createDateObjectFromString(cursor.getString(dateIndex)));
                event.setTime(event.getTime().createTimeObjectFromString(cursor.getString(timeIndex)));
                event.setPrice(cursor.getString(priceIndex));
                event.setLocation(cursor.getString(locationIndex));
                event.setImageUrl(cursor.getString(imageUrlIndex));
                event.setLastUpDateTime(cursor.getDouble(lastUpdateDateIndex));
                list.add(event);
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addEvent(SQLiteDatabase db, Event event) {
        ContentValues values = new ContentValues();
        values.put(EVENT_ID, event.getId());
        values.put(EVENT_NAME, event.getName());
        values.put(EVENT_DATE, event.getDate().toString());
        values.put(EVENT_TIME, event.getTime().toString());
        values.put(EVENT_PRICE, event.getPrice());
        values.put(EVENT_LOCATION, event.getLocation());
        values.put(EVENT_IMAGE_URL, event.getImageUrl());
        values.put(EVENT_LAST_UPDATE, event.getLastUpDateTime());
        db.insert(EVENT_TABLE, EVENT_ID, values);
    }

    static Event getEvent(SQLiteDatabase db, String id) {
        return null;
    }

    static public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + EVENT_TABLE +
                " (" +
                EVENT_ID + " TEXT PRIMARY KEY, " +
                EVENT_NAME + " TEXT, " +
                EVENT_DATE + " TEXT, " +
                EVENT_TIME + " TEXT, " +
                EVENT_PRICE + " TEXT, " +
                EVENT_LOCATION + " TEXT, " +
                EVENT_LAST_UPDATE + " NUMBER, " +
                EVENT_IMAGE_URL + " TEXT);";
        Log.d("TAG", sql);
        db.execSQL(sql);
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + EVENT_TABLE + ";");
        onCreate(db);
    }


}
