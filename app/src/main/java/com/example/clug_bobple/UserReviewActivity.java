package com.example.clug_bobple;

import android.app.Person;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserReviewActivity extends AppCompatActivity {
    int len, cnt = 1, sum = 0;
    ImageView more_review_db, back_button;
    String url;
    //int mScrollPosition;
    FloatingActionButton review_add_button;
    double lat, lon;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ReviewAdapter adapter = new ReviewAdapter();

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("lat", 0);
        lon = intent.getDoubleExtra("lon", 0);
        name = intent.getStringExtra("name");

        adapter.clear();
        adapter.notifyDataSetChanged();

        JSONObject reviewItems = new JSONObject();
        try {
            reviewItems.put("lat", Double.toString(lat));
            reviewItems.put("long", Double.toString(lon));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        url = getString(R.string.url) + "/restaurant/" + name + "/reviews/" + Integer.toString(cnt);
        //Toast.makeText(UserReviewActivity.this, url, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(UserReviewActivity.this);


        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, reviewItems, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    len = (int) response.get("length");
                    JSONArray reviews = response.getJSONArray("reviews");

                    for (int i = 0; i < len; i++) {
                        if (cnt == 1) {
                            JSONObject object = reviews.getJSONObject(i);
                            adapter.addItem(new Review(object.get("name").toString(), object.get("date").toString(),
                                    object.get("content").toString(), Integer.parseInt(object.get("rate").toString())));
                            if (i == len - 1) cnt += 1;
                        }
                    }
                    sum += len;
                    recyclerView.setAdapter(adapter);
                    //mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

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

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    url = getString(R.string.url) + "/restaurant/" + name + "/reviews/" + Integer.toString(cnt);
                    //Toast.makeText(UserReviewActivity.this, url, Toast.LENGTH_SHORT).show();

                    if (len == 0){
                        //Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                    } else {
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
                                    sum += len;
                                    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
                                        @Override
                                        protected int getVerticalSnapPreference() {
                                            return LinearSmoothScroller.SNAP_TO_END;
                                        }
                                    };
                                    linearSmoothScroller.setTargetPosition(sum);
                                    recyclerView.getLayoutManager().startSmoothScroll(linearSmoothScroller);
                                    recyclerView.setAdapter(adapter);
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
                }
            }
        });

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new OnReviewItemClickListener(){
            @Override
            public void onItemClick(ReviewAdapter.ViewHolder holder, View view, int position) {
                Review item = adapter.getItem(position);
            }
        });

        review_add_button = findViewById(R.id.review_add_button);
        review_add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserReviewActivity.this, ReviewWriteActivity.class);
                intent1.putExtra("lon", lon);
                intent1.putExtra("lat", lat);
                intent1.putExtra("restaurant", name);
                //finish();
                startActivityForResult(intent1, 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent2 = new Intent(this, GMap.class);
        finish();
        startActivity(intent2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 0:
                Intent intent3 = getIntent();
                name = intent3.getStringExtra("name");
                lon = intent3.getDoubleExtra("lon", 0);
                lat = intent3.getDoubleExtra("lat", 0);
                intent3.putExtra("name", name);
                intent3.putExtra("lon", lon);
                intent3.putExtra("lat", lat);
                finish();
                startActivity(intent3);
        }
    }
}