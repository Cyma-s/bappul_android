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

                JSONObject testjson = new JSONObject();
                try{
                    testjson.put("id", id);
                    testjson.put("password", password);
                    testjson.put("name", name);
                    testjson.put("entranceYear", std_num);
                    testjson.put("gender", sex);
                    testjson.put("department", dep);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(Sign_upActivity.this);
                String url = "http://580aae36ebac.ngrok.io/users";

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, testjson,
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(Sign_upActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                if (response.get("answer").toString().equals("true")){
                                    Intent intent1 = new Intent(Sign_upActivity.this, LoginActivity.class);
                                    startActivity(intent1);
                                } else {
                                    Toast.makeText(Sign_upActivity.this, "옳지 않은 접근입니다.", Toast.LENGTH_SHORT).show();
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
}