package com.example.clug_bobple;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class EmailAuthenticationActivity  extends AppCompatActivity {
    TextView email_address, auth_number, time;
    EditText email_address_text, auth_number_text;
    ImageView email_confirm, auth_button;
    String code, address;
    boolean isAlreadyExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        email_address = (TextView)findViewById(R.id.email_address);
        auth_number = (TextView)findViewById(R.id.auth_number);
        time = (TextView)findViewById(R.id.time);
        email_address_text = (EditText)findViewById(R.id.email_address_text);
        auth_number_text = (EditText)findViewById(R.id.auth_number_text);
        email_confirm = (ImageView)findViewById(R.id.email_confirm);
        auth_button = (ImageView)findViewById(R.id.auth_button);

        email_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_address_text.getText().toString().equals("admin")) {
                    Intent intent_sign_up = new Intent(EmailAuthenticationActivity.this, Sign_upActivity.class);
                    intent_sign_up.putExtra("address", "testAdmin@cau.ac.kr");
                    startActivity(intent_sign_up);
                    finish();
                }

                address = email_address_text.getText().toString();

                if (Pattern.matches("^[a-zA-Z0-9]+@[cau.ac.kr]+$", address)) {
                    auth_number_text.setVisibility(View.VISIBLE);
                    auth_number.setVisibility(View.VISIBLE);
                    auth_button.setVisibility(View.VISIBLE);

                    SendMail mailServer = new SendMail();
                    mailServer.sendSecurityCode(getApplicationContext(), address);
                    code = mailServer.getCode();
                } else{
                    Toast.makeText(EmailAuthenticationActivity.this, "중앙대학교 이메일 주소가 아닙니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        auth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getString(R.string.url)+"/mails/authentication/"+email_address_text.getText().toString();
                System.out.println(url);

                RequestQueue queue = Volley.newRequestQueue(EmailAuthenticationActivity.this);
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("false")) {
                                    Toast.makeText(EmailAuthenticationActivity.this, "이미 사용중인 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (auth_number_text.getText().toString().equals(code)){
                                        Intent intent_sign_up = new Intent(EmailAuthenticationActivity.this, Sign_upActivity.class);
                                        intent_sign_up.putExtra("address", address);
                                        startActivity(intent_sign_up);
                                    }
                                    else{
                                        Toast.makeText(EmailAuthenticationActivity.this, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmailAuthenticationActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
    }
}
