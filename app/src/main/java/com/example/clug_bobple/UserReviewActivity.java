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

        adapter.addItem(new Review("김은솔", "2월 4일", "안녕하세요", 4));
        adapter.addItem(new Review("김재훈", "1월 4일", "안녕하세요ㅎㅇ", 1));
        adapter.addItem(new Review("전성수", "3월 4일", "안녕하세요ㅋ", 2));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋㄹㄴㅁㅇㄻㄴㅇㄹㅋㄹㅋㅇㄴㄹㅋㄴㅇㄹㅋㄴㅇㄹㄴㅋㄹㅋㄴㅇㄹㅋㄴㄹㅋㄴㄹㅇㅋㄴㄹ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));
        adapter.addItem(new Review("김은재", "6월 4일", "안녕하세요ㅋㅋ", 5));


        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnReviewItemClickListener(){
            @Override
            public void onItemClick(ReviewAdapter.ViewHolder holder, View view, int position) {
                Review item = adapter.getItem(position);
            }
        });

    }
}
