/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.cambio.finalprojectandroid.activitiys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cambio.finalprojectandroid.R;
import com.cambio.finalprojectandroid.model.CallBackInterface;
import com.cambio.finalprojectandroid.model.Model;
import com.cambio.finalprojectandroid.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Model.getInstance();
        Button btn_login = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        Button btn_register = (Button) findViewById(R.id.register_btn);


        if (Model.instance != null) {
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // register new user
                    final String userId = Model.instance.getModelFirebase().getFirebaseUserEntityId();
                    final String userEmail = ((EditText) findViewById(R.id.register_email)).getText().toString();
                    final String userPassword = ((EditText) findViewById(R.id.register_password)).getText().toString();
                    if ((userEmail != null && !userEmail.equals("")) || (userPassword != null && !userPassword.equals(""))) {
                        if (userEmail != null && !userEmail.equals("")) {
                            if (userPassword != null && !userPassword.equals("")) {
                                final User newUser = new User(userId, userEmail, userPassword);
                                Model.instance.getModelFirebase().registerAccount(RegisterActivity.this, userEmail, userPassword, userId, new CallBackInterface.RegisterUserCallBack() {
                                    @Override
                                    public void onComplete(FirebaseUser user, Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User profile updated -> " + user.getDisplayName());
                                            Model.instance.getModelFirebase().addUser(newUser);
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Please Insert Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please Insert Email", Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Please Insert Email And Password", Toast.LENGTH_SHORT).show();
                    }
                }


            });

        }


    }


}
