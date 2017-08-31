package com.cambio.finalprojectandroid.activitiys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cambio.finalprojectandroid.R;
import com.cambio.finalprojectandroid.model.Model;

public class HomeActivity extends Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button logout = (Button) findViewById(R.id.home_btn_logout);
        Button toList = (Button) findViewById(R.id.home_btn_toList);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("email");
        TextView txtView = (TextView) findViewById(R.id.home_email);
        txtView.setText(message);
        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.getModelFirebase().signOut();
                finish();
            }
        });





    }







}