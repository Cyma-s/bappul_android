package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
                finish();
            }
        });



    }

    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500){
            finish();
            Toast.makeText(this, "감사합니다 :)", Toast.LENGTH_LONG).show();
        }
    }
}