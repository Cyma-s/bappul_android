package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
    private EditText user_id;
    private EditText user_password;
    private ImageView logintosignup;
    private ImageView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (EditText) findViewById(R.id.user_id);
        user_password = (EditText)findViewById(R.id.user_password);
        logintosignup = (ImageView)findViewById(R.id.logintosignup);
        logintosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_logintosignup = new Intent(LoginActivity.this, Sign_upActivity.class);
                startActivity(intent_logintosignup);
            }
        });

        login = (ImageView)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = user_id.getText().toString();
                String password = user_password.getText().toString();

            }
        });

    }
}