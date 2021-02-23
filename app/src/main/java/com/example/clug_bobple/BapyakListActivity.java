package com.example.clug_bobple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    public static Context CONTEXT;
    int len = 0, cnt = 1, sum = 0;
    ImageView more_review_db;
    String url, content, id;
    String post_type, gender, major;
    Spinner type_clsfc, gender_clsfc, major_clsfc;
    FloatingActionButton post_add_button;
    ImageView bapyak_search, bapyak_search_button, back_button;
    EditText search_content;
    RecyclerView recyclerView;
    int type_clsfc_num = 0, gender_clsfc_num = 0, major_clsfc_num = 0;
    ArrayAdapter<CharSequence> type_adapter, gender_adapter, major_adapter;
    SharedPreferences sharedPreferences;
    boolean isSearchClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bapyak_list);

        CONTEXT = this;

        recyclerView = findViewById(R.id.bapyak_recyclerview);
        bapyak_search_button = findViewById(R.id.bapyak_search_button);
        search_content = findViewById(R.id.search_content);
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
        BapyakAdapter adapter = new BapyakAdapter();

        type_clsfc = findViewById(R.id.type_clsfc);
        gender_clsfc = findViewById(R.id.gender_clsfc);
        major_clsfc = findViewById(R.id.major_clsfc);

        Intent intent = getIntent();
        type_clsfc_num = intent.getIntExtra("type", 0);
        gender_clsfc_num = intent.getIntExtra("gender", 0);
        major_clsfc_num = intent.getIntExtra("major", 0);

        type_clsfc.setSelection(type_clsfc_num);
        gender_clsfc.setSelection(gender_clsfc_num);
        major_clsfc.setSelection(major_clsfc_num);

        type_adapter = ArrayAdapter.createFromResource(this,
                R.array.bapyak_clsfc, android.R.layout.simple_spinner_item);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_clsfc.setAdapter(type_adapter);
        type_clsfc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_clsfc_num = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender_adapter = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);

        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_clsfc.setAdapter(gender_adapter);
        gender_clsfc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_clsfc_num = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        major_adapter = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.simple_spinner_item);

        major_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major_clsfc.setAdapter(major_adapter);
        major_clsfc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                major_clsfc_num = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        adapter.notifyDataSetChanged();
        loadingPost(post_type, gender, major, recyclerView, adapter);

        bapyak_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = search_content.getText().toString();

                isSearchClicked = true;

                adapter.clear();
                adapter.notifyDataSetChanged();
                cnt = 1;
                url = getString(R.string.url) + "/bapyak/" + post_type + "/search/"+
                        content + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;

                searchPosts(url, post_type, gender, major, recyclerView, adapter);
            }
        });

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

                if (isSearchClicked){
                    Intent intent = new Intent(BapyakListActivity.this, BapyakListActivity.class);
                    intent.putExtra("type", type_clsfc_num);
                    intent.putExtra("gender", gender_clsfc_num);
                    intent.putExtra("major", major_clsfc_num);

                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    cnt = 1;
                    url = getString(R.string.url) + "/bapyak/" + post_type + "/search/"+
                            content + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;

                    searchPosts(url, post_type, gender, major, recyclerView, adapter);
                } else {
                    Intent intent = new Intent(BapyakListActivity.this, BapyakListActivity.class);
                    intent.putExtra("type", type_clsfc_num);
                    intent.putExtra("gender", gender_clsfc_num);
                    intent.putExtra("major", major_clsfc_num);

                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    cnt = 1;

                    loadingPost(post_type, gender, major, recyclerView, adapter);
                }
            }
        });

        adapter.setOnItemClicklistener(new OnBapyakItemClickListener() {
            @Override
            public void onItemClick(BapyakAdapter.ViewHolder holder, View view, int position) {
                Bapyak item = adapter.getItem(position);
                id = item.getContent_id();
                Intent intent1 = new Intent(BapyakListActivity.this, BapyakContentActivity.class);
                intent1.putExtra("content_id", id);
                startActivity(intent1);
            }
        });


        post_add_button = findViewById(R.id.post_add_button);
        post_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BapyakListActivity.this, bapyak_write.class);
                finish();
                startActivity(intent1);
            }
        });
    }

    public void loadingPost (String post_type, String gender, String major, RecyclerView recyclerView,
                             BapyakAdapter adapter){
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        url = getString(R.string.url) + "/bapyak/" + post_type + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
        //Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(BapyakListActivity.this);

        jsonRequest(queue, adapter, token);

        morePostLoad(adapter, queue, token, isSearchClicked);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void jsonRequest(RequestQueue queue, BapyakAdapter adapter, String token){
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            len = (int) response.get("length");
                            JSONArray posts =  response.getJSONArray("bapyaks");

                            //Toast.makeText(BapyakListActivity.this, Integer.toString(len), Toast.LENGTH_LONG).show();
                            //Toast.makeText(BapyakListActivity.this, posts.toString(), Toast.LENGTH_LONG).show();


                            for (int i = 0; i < len; i++) {
                                if (cnt == 1) {
                                    JSONObject object = posts.getJSONObject(i);
                                    adapter.addItem(new Bapyak(object.get("title").toString(), object.get("userName").toString(),
                                            object.get("userEntranceYear").toString() + "학번", object.get("createdDate").toString(), object.get("comentNum").toString(), object.get("id").toString()));
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
        queue.add(jsonObjectRequest);
    }

    public void morePostLoad(BapyakAdapter adapter, RequestQueue queue, String token, boolean isSearchClicked){
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    if (isSearchClicked){
                        url = getString(R.string.url) + "/bapyak/" + post_type + "/search/"+
                                content + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                    } else {
                        url = getString(R.string.url) + "/bapyak/" + post_type + "/" + Integer.toString(cnt) + "?gender=" + gender + "&major=" + major;
                    }
                    //Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_SHORT).show();

                    if (len == 0){
                        //Toast.makeText(UserReviewActivity.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    len = (int) response.get("length");
                                    JSONArray posts = response.getJSONArray("bapyaks");

                                    for (int i = 0; i<len; i++){
                                        JSONObject more_post = posts.getJSONObject(i);
                                        adapter.addItem(new Bapyak(more_post.get("title").toString(), more_post.get("userName").toString(),
                                                more_post.get("userEntranceYear").toString() + "학번", more_post.get("createdDate").toString(), more_post.get("comentNum").toString(), more_post.get("id").toString()));
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

                        queue.add(jsonObjectRequest1);
                    }
                }
            }
        });
    }

    public void searchPosts(String url, String post_type, String gender, String major, RecyclerView recyclerView,
                            BapyakAdapter adapter) {
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        //Toast.makeText(BapyakListActivity.this, url, Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(BapyakListActivity.this);

        jsonRequest(queue, adapter, token);

        morePostLoad(adapter, queue, token, isSearchClicked);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        type_adapter.notifyDataSetChanged();
        gender_adapter.notifyDataSetChanged();
        major_adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}