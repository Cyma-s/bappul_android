package com.example.clug_bobple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BapyakListActivity extends AppCompatActivity {
    int len = 0, cnt = 1, sum = 0;
    ImageView more_review_db;
    String url;
    String post_type, gender, major;
    Spinner type_clsfc, gender_clsfc, major_clsfc;
    FloatingActionButton post_add_button;
    ImageView bapyak_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bapyak_list);

        RecyclerView recyclerView = findViewById(R.id.bapyak_recyclerview);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BapyakAdapter adapter = new BapyakAdapter();

        type_clsfc = findViewById(R.id.type_clsfc);
        gender_clsfc = findViewById(R.id.gender_clsfc);
        major_clsfc = findViewById(R.id.major_clsfc);

        type_clsfc.setSelection(0);
        gender_clsfc.setSelection(0);
        major_clsfc.setSelection(0);

        post_type = type_clsfc.getSelectedItem().toString();
        gender = gender_clsfc.getSelectedItem().toString();
        major = major_clsfc.getSelectedItem().toString();

        if (gender.equals("성별")) {
            gender = "none";
        }

        if (post_type.equals("유형")){
            post_type = "none";
        } else if(post_type.equals("사주세요")){
            post_type = "join";
        } else if(post_type.equals("사줄게요")){
            post_type = "inviting";
        }

        if (major.equals("학과")) {
            major = "none";
        }

        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
        Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(BapyakListActivity.this);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            len = Integer.parseInt(response.get(1).toString());
                            JSONArray posts = (JSONArray)response.get(0);

                            Toast.makeText(BapyakListActivity.this, Integer.toString(len), Toast.LENGTH_LONG).show();

                            for (int i = 0; i < len; i++) {
                                if (cnt == 1) {
                                    JSONObject object = (JSONObject) posts.get(i);
                                    adapter.addItem(new Bapyak(object.get("title").toString(), object.get("userName").toString(),
                                            object.get("userEntranceYear").toString(), object.get("createdDate").toString()));
                                    if (i == len - 1) cnt += 1;
                                }
                            }
                            sum += len;
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BapyakListActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        queue.add(jsonArrayRequest);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                    Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_SHORT).show();

                    if (len == 0){
                        //Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        final JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    len = Integer.parseInt(response.get(1).toString());
                                    JSONArray posts = (JSONArray) response.get(0);

                                    for (int i = 0; i<len; i++){
                                        JSONObject more_post = (JSONObject) posts.get(i);
                                        adapter.addItem(new Bapyak(more_post.get("title").toString(), more_post.get("name").toString(),
                                                more_post.get("entrance_year").toString(), more_post.get("createdDate").toString()));
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
                                Toast.makeText(BapyakListActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> heads = new HashMap<String, String>();
                                heads.put("Authorization", "Bearer " + token);
                                return heads;
                            }
                        };

                        queue.add(jsonArrayRequest1);
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
        //loadingPost(post_type, gender, major, recyclerView, adapter);

        bapyak_search = findViewById(R.id.bapyak_search);
        bapyak_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_type = type_clsfc.getSelectedItem().toString();
                gender = gender_clsfc.getSelectedItem().toString();
                major = major_clsfc.getSelectedItem().toString();

                if (gender.equals("성별")) {
                    gender = "none";
                }

                if (post_type.equals("유형")){
                    post_type = "none";
                } else if(post_type.equals("사주세요")){
                    post_type = "join";
                } else if(post_type.equals("사줄게요")){
                    post_type = "inviting";
                }

                if (major.equals("학과")) {
                    major = "none";
                }
                // loadingPost(post_type, gender, major, recyclerView, adapter);

                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreferences.getString("Authorization", "");
                url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_LONG).show();
                RequestQueue queue = Volley.newRequestQueue(BapyakListActivity.this);

                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    len = Integer.parseInt(response.get(1).toString());
                                    JSONArray posts = (JSONArray)response.get(0);

                                    Toast.makeText(BapyakListActivity.this, Integer.toString(len), Toast.LENGTH_LONG).show();

                                    for (int i = 0; i < len; i++) {
                                        if (cnt == 1) {
                                            JSONObject object = (JSONObject) posts.get(i);
                                            adapter.addItem(new Bapyak(object.get("title").toString(), object.get("userName").toString(),
                                                    object.get("userEntranceYear").toString(), object.get("createdDate").toString()));
                                            if (i == len - 1) cnt += 1;
                                        }
                                    }
                                    sum += len;
                                    recyclerView.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BapyakListActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> heads = new HashMap<String, String>();
                        heads.put("Authorization", "Bearer " + token);
                        return heads;
                    }
                };

                queue.add(jsonArrayRequest);

                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically(1)){
                            url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                            Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_SHORT).show();

                            if (len == 0){
                                //Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                final JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>(){
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try{
                                            len = Integer.parseInt(response.get(1).toString());
                                            JSONArray posts = (JSONArray) response.get(0);

                                            for (int i = 0; i<len; i++){
                                                JSONObject more_post = (JSONObject) posts.get(i);
                                                adapter.addItem(new Bapyak(more_post.get("title").toString(), more_post.get("name").toString(),
                                                        more_post.get("entrance_year").toString(), more_post.get("createdDate").toString()));
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
                                        Toast.makeText(BapyakListActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> heads = new HashMap<String, String>();
                                        heads.put("Authorization", "Bearer " + token);
                                        return heads;
                                    }
                                };

                                queue.add(jsonArrayRequest1);
                            }
                        }
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        });



        post_add_button = findViewById(R.id.post_add_button);
        post_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void loadingPost (String post_type, String gender, String major, RecyclerView recyclerView,
                             BapyakAdapter adapter){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
        Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(BapyakListActivity.this);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            len = Integer.parseInt(response.get(1).toString());
                            JSONArray posts = (JSONArray)response.get(0);

                            Toast.makeText(BapyakListActivity.this, Integer.toString(len), Toast.LENGTH_LONG).show();

                            for (int i = 0; i < len; i++) {
                                if (cnt == 1) {
                                    JSONObject object = (JSONObject) posts.get(i);
                                    adapter.addItem(new Bapyak(object.get("title").toString(), object.get("userName").toString(),
                                            object.get("userEntranceYear").toString(), object.get("createdDate").toString()));
                                    if (i == len - 1) cnt += 1;
                                }
                            }
                            sum += len;
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BapyakListActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        queue.add(jsonArrayRequest);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    url = getString(R.string.url) + "/bapyak/" + "join" + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                    Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_SHORT).show();

                    if (len == 0){
                        //Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        final JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    len = Integer.parseInt(response.get(1).toString());
                                    JSONArray posts = (JSONArray) response.get(0);

                                    for (int i = 0; i<len; i++){
                                        JSONObject more_post = (JSONObject) posts.get(i);
                                        adapter.addItem(new Bapyak(more_post.get("title").toString(), more_post.get("name").toString(),
                                                more_post.get("entrance_year").toString(), more_post.get("createdDate").toString()));
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
                                Toast.makeText(BapyakListActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> heads = new HashMap<String, String>();
                                heads.put("Authorization", "Bearer " + token);
                                return heads;
                            }
                        };

                        queue.add(jsonArrayRequest1);
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

}