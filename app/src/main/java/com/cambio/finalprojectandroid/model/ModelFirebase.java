package com.cambio.finalprojectandroid.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cambio.finalprojectandroid.activitiys.LoginActivity;
import com.cambio.finalprojectandroid.activitiys.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import java.util.Map;

/**
 * Created by dvirh on 8/24/2017.
 */


public class ModelFirebase {

    ChildEventListener eventListener;
    private FirebaseAuth mAuth;


    public void addEvent(Event event) {
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


    public void saveImage(Bitmap imageBmp, String name, final CallBackInterface.SaveImageListener listener) {
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

    public void getImage(String url, final CallBackInterface.GetImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d("TAG", exception.getMessage());
                listener.onFail();
            }
        });
    }


    public void synchAndRegisterEventData(double lastUpdateDate,
                                          final CallBackInterface.RegisterEventsUpdatesCallback callback) {
        if (eventListener != null) {
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
                        Log.d("TAG", "onChildAdded called " + s);
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

    public String getFirebaseEventEntityId() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");
        String newKey = myRef.push().getKey();

        return newKey;

    }

    public String getFirebaseUserEntityId() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String newKey = myRef.push().getKey();

        return newKey;

    }


    public static void addUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Map<String, Object> value = new HashMap<>();
        value.put("id", user.getUserId());
        value.put("userEmail", user.getUserEmail());
        value.put("userPassword", user.getUserPassword());

        myRef.child(user.getUserId()).setValue(value);
    }




    public static void getUser(String accountId, final CallBackInterface.GetUserCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Log.d("TAG", "accountId is " + accountId);
        myRef.child(accountId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }


    public void registerAccount(final RegisterActivity registerActivity, final String email, final String password, final String id, final CallBackInterface.RegisterUserCallBack callBack) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registerActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "createUserWithEmail:success -> " + email);
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(id).build();
                            if (user != null) {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                callBack.onComplete(user, task);

                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(registerActivity, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loginAccount(final LoginActivity loginActivity, final String email, final String password,final ProgressBar progressBar, final CallBackInterface.LoginUserCallBack callBack) {
        mAuth = FirebaseAuth.getInstance();
        if (!email.isEmpty()) {
            if (!password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callBack.onComplete(task);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(loginActivity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            } else {
                Toast.makeText(loginActivity, "Password Is Empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(loginActivity, "Email Is Empty", Toast.LENGTH_SHORT).show();
        }

    }

    public static void signOut() {

        Model.instance.signOut();
        FirebaseAuth.getInstance().signOut();
    }


}
