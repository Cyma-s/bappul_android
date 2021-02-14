package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

                Intent intent = new Intent(LoginActivity.this, UserReviewActivity.class);
                startActivity(intent);

                JSONObject infoForLogin = new JSONObject();
                try {
                    infoForLogin.put("id", id);
                    infoForLogin.put("password", password);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }

                String url = getString(R.string.url) + "/auth/login";
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, infoForLogin, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            saveToken(response);
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        logIn();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "등록되지 않은 아이디/비밀번호 입니다. 다시 한번 확인해 주세요:)", Toast.LENGTH_LONG).show();
                    }
                });

                queue.add(jsonObjectRequest);
            }
        });
    }

    private void saveToken(JSONObject response) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Authorization", response.get("access_token").toString());
        editor.apply();
    }

    private void logIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        String url = getString(R.string.url) + "/profile";
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent logIn = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(logIn);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer "+token);
                return heads;
            }
        };
        queue.add(jsonObjectRequest);
    }
}