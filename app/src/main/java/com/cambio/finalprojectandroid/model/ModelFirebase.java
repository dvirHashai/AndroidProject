package com.cambio.finalprojectandroid.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.cambio.finalprojectandroid.utils.Date;
import com.cambio.finalprojectandroid.utils.Time;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dvirh on 8/24/2017.
 */

public class ModelFirebase {
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

    public String getFirebaseEntityId(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        String newKey = myRef.push().getKey();

        return newKey;

    }
}
