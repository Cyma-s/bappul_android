package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BapyakContentActivity extends AppCompatActivity {
    String content_id;
    TextView entrance_year, user_name, date, title, content, comments_num;
    RecyclerView recyclerView;
    EditText comment;
    ImageView send;
    String url;
    int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bapyak_content);

        recyclerView = findViewById(R.id.comment_recyclerView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CommentAdapter adapter = new CommentAdapter();

        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");

        entrance_year = findViewById(R.id.content_entrance);
        user_name = findViewById(R.id.content_username);
        date = findViewById(R.id.content_date);
        title = findViewById(R.id.content_title);
        content = findViewById(R.id.content_content);
        comments_num = findViewById(R.id.comments_num);
        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send_button);

        String content_url = getString(R.string.url) + "/bapyak/"+content_id;
        RequestQueue queue = Volley.newRequestQueue(BapyakContentActivity.this);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, content_url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    user_name.setText(response.get("userName").toString());
                    entrance_year.setText(response.get("userEntranceYear").toString());
                    date.setText(response.get("createdDate").toString());
                    title.setText(response.get("title").toString());
                    comments_num.setText(response.get("comentNum").toString());
                    content.setText(response.get("content").toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BapyakContentActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);

        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");

        url = getString(R.string.url) + "/bapyakcomment/"+content_id;
        RequestQueue queue1 = Volley.newRequestQueue(BapyakContentActivity.this);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            len = response.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject object = response.getJSONObject(i);
                                adapter.addItem(new Comment(object.get("name").toString(), object.get("entranceYear").toString()+"학번",
                                        object.get("content").toString(), object.get("date").toString()));
                            }
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BapyakContentActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        queue1.add(jsonArrayRequest);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        RequestQueue queue2 = Volley.newRequestQueue(BapyakContentActivity.this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                String url = getString(R.string.url) + "/bapyakcomment/"+content_id;
                JSONObject postJson = new JSONObject();
                try{
                    postJson.put("content", comment.getText().toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(BapyakContentActivity.this, postJson.toString(), Toast.LENGTH_LONG).show();

                comment.setText(null);


                //addItem 해주면 될 것 같은데 아직 서버에서 안 줄 것 같음.
                final JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    len = response.length();
                                    for (int i = 0; i < len; i++) {
                                        JSONObject object = response.getJSONObject(i);
                                        adapter.addItem(new Comment(object.get("name").toString(), object.get("entranceYear").toString()+"학번",
                                                object.get("content").toString(), object.get("date").toString()));
                                    }
                                    recyclerView.setAdapter(adapter);
                                    Toast.makeText(BapyakContentActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BapyakContentActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> heads = new HashMap<String, String>();
                        heads.put("Authorization", "Bearer "+token);
                        return heads;
                    }
                };

                queue2.add(jsonArrayRequest1);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}