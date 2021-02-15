package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView btn_sign_up;
    private ImageView btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_up = (ImageView) findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_auth = new Intent(MainActivity.this, EmailAuthenticationActivity.class);
                startActivity(intent_auth);
            }
        });

        btn_login = (ImageView)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });



    }
}