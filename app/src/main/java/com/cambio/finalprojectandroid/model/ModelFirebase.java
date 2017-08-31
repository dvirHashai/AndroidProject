package com.cambio.finalprojectandroid.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dvirh on 8/24/2017.
 *
 */


public class ModelFirebase {

    ChildEventListener eventListener;
    public  void addEvent(Event event) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        Map<String, Object> value = new HashMap<>();
        value.put("id", event.getId());
        value.put("name", event.getName());
        value.put("date", event.getDate());
        value.put("time", event.getTime());
        value.put("price", event.getPrice());
        value.put("location", event.getLocation());
        value.put("imageUrl", event.getImageUrl());
        value.put("lastUpdateDate", ServerValue.TIMESTAMP);
        myRef.child(event.getId()).setValue(value);
    }

    public void deleteEventItem(String eventId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        myRef.child(eventId).removeValue();
    }

    interface GetEventCallback {
        void onComplete(Event event);

        void onCancel();
    }

    public void getEvent(String stId, final GetEventCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        myRef.child(stId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                callback.onComplete(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }




    public void saveImage(Bitmap imageBmp, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG",exception.getMessage());
                listener.onFail();
            }
        });
    }




    interface RegisterEventsUpdatesCallback{
        void onEventUpdate(Event event , DataStateChange StateChange);
    }
    public void synchAndRegisterEventData(double lastUpdateDate,
                                        final RegisterEventsUpdatesCallback callback) {
        if(eventListener != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("events");
            myRef.removeEventListener(eventListener);
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate);
        eventListener = myRef.orderByChild("lastUpdateDate").startAt(lastUpdateDate)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("TAG","onChildAdded called " + s);
                        Event event = dataSnapshot.getValue(Event.class);
                        callback.onEventUpdate(event, DataStateChange.ADDED);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Event event = dataSnapshot.getValue(Event.class);
                        callback.onEventUpdate(event, DataStateChange.CHANGED);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Event event = dataSnapshot.getValue(Event.class);
                        callback.onEventUpdate(event, DataStateChange.REMOVED);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Event event = dataSnapshot.getValue(Event.class);
                        callback.onEventUpdate(event, DataStateChange.MOVED);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public String getFirebaseEntityId(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        String newKey = myRef.push().getKey();

        return newKey;

    }
}
