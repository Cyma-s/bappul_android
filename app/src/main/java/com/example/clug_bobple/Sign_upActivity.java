package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

            }
        });

    }
}