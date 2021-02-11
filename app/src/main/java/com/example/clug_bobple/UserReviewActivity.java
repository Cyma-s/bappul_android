package com.example.clug_bobple;

import android.app.Person;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ReviewAdapter adapter = new ReviewAdapter();

        adapter.addItem(new Review("김은솔", "2월 4일", "안녕하세요"));
        adapter.addItem(new Review("김재훈", "1월 4일", "안녕하세요ㅎㅇ"));
        adapter.addItem(new Review("전성수", "3월 4일", "안녕하세요ㅋ"));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ"));
        adapter.addItem(new Review("김민", "2월 4일", "안녕하세요!"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇsdfasdhfkljahsklejhfkljashefjkldhasjklfhalkjsdhflkㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));
        adapter.addItem(new Review("김윤", "2월 4일", "안녕하세요ㅇㅇ"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnReviewItemClickListener(){
            @Override
            public void onItemClick(ReviewAdapter.ViewHolder holder, View view, int position) {
                Review item = adapter.getItem(position);
            }
        });

    }
}
