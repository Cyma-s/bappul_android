package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONObject;

public class BapyakListActivity extends AppCompatActivity {
    int len, cnt = 1;
    ImageView more_review_db;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bapyak_list);

        RecyclerView recyclerView = findViewById(R.id.bapyak_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ReviewAdapter adapter = new ReviewAdapter();


    }
}