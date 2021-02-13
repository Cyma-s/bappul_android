package com.example.clug_bobple;

import android.app.Person;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserReviewActivity extends AppCompatActivity {
    int len, cnt = 1;
    ImageView more_review_db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ReviewAdapter adapter = new ReviewAdapter();

        double lat = 37.5061724;
        double lon = 126.9569251;
        String name = "차돌이식당";


        JSONObject reviewItems = new JSONObject();
        try {
            reviewItems.put("lat", Double.toString(lat));
            reviewItems.put("long", Double.toString(lon));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        String url = getString(R.string.url) + "/restaurant/" + name + "/reviews/" + Integer.toString(cnt);
        RequestQueue queue = Volley.newRequestQueue(UserReviewActivity.this);

        more_review_db = findViewById(R.id.more_review_db);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, reviewItems, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    len = (int) response.get("length");

                    JSONArray reviews = response.getJSONArray("reviews");

                    for (int i = 0; i < len; i++) {
                        if (cnt == 1){
                            JSONObject object = reviews.getJSONObject(i);
                            adapter.addItem(new Review(object.get("name").toString(), object.get("date").toString(),
                                    object.get("content").toString(), Integer.parseInt(object.get("rate").toString())));
                            if (i == len-1) cnt += 1;
                        }
                    }

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserReviewActivity.this, "등록되지 않은 리뷰입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);


        more_review_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, url, reviewItems, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            len = (int) response.get("length");
                            JSONArray reviews = response.getJSONArray("reviews");
                            for (int i = 0; i<len; i++){
                                JSONObject more_review = reviews.getJSONObject(i);
                                adapter.addItem(new Review(more_review.get("name").toString(), more_review.get("date").toString(),
                                        more_review.get("content").toString(), Integer.parseInt(more_review.get("rate").toString())));
                                if (i == len-1) cnt += 1;
                            }
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonObjectRequest1);
            }
        });

        /*adapter.addItem(new Review("김은솔", "2월 4일", "안녕하세요", 4));
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
*/

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnReviewItemClickListener(){
            @Override
            public void onItemClick(ReviewAdapter.ViewHolder holder, View view, int position) {
                Review item = adapter.getItem(position);
            }
        });

    }
}