package com.cambio.finalprojectandroid.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Chen on 30/08/2017.
 */

public class UserFireBase {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    //    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String USERS_TABLE = "Users";
    private static DatabaseReference myRef = database.getReference(USERS_TABLE);


}
