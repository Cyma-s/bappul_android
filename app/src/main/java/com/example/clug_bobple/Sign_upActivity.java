package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOError;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_upActivity extends AppCompatActivity {
    private ImageView duplication;
    private ImageView signup;
    private EditText name_signup;
    private EditText id_signup;
    private EditText password_signup;
    private Spinner spinner_std;
    private Spinner spinner_sex;
    private Spinner spinner_dep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        String user_adderess = intent.getStringExtra("address");

        duplication = (ImageView)findViewById(R.id.duplication);
        signup = (ImageView) findViewById(R.id.signup);
        name_signup = (EditText)findViewById(R.id.name_signup);
        id_signup = (EditText)findViewById(R.id.id_signup);
        password_signup = (EditText)findViewById(R.id.password_signup);
        spinner_std = (Spinner)findViewById(R.id.spinner_std);
        spinner_sex = (Spinner)findViewById(R.id.spinner_sex);
        spinner_dep = (Spinner)findViewById(R.id.spinner_dep);

        duplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = id_signup.getText().toString();
                String url = getString(R.string.url) + "/users/authentication/" +id;

                RequestQueue queue = Volley.newRequestQueue(Sign_upActivity.this);
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                if(response.equals("true"))
                                    Toast.makeText(Sign_upActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Sign_upActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Sign_upActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = id_signup.getText().toString();
                String password = password_signup.getText().toString();
                String name = name_signup.getText().toString();
                String std_num = spinner_std.getSelectedItem().toString();
                String sex = spinner_sex.getSelectedItem().toString();
                String dep = spinner_dep.getSelectedItem().toString();

                JSONObject userJson = new JSONObject();
                try{
                    userJson.put("id", id);
                    userJson.put("password", password);
                    userJson.put("name", name);
                    userJson.put("entranceYear", std_num);
                    userJson.put("gender", sex);
                    userJson.put("department", dep);
                    userJson.put("mailAddress", user_adderess);
                    System.out.println(user_adderess);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(Sign_upActivity.this);
                String url = getString(R.string.url)+"/users";

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userJson,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.get("answer").toString().equals("true")){
                                    Toast.makeText(Sign_upActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(Sign_upActivity.this, LoginActivity.class);
                                    startActivity(intent1);
                                } else {
                                    Toast.makeText(Sign_upActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Sign_upActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonObjectRequest);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}