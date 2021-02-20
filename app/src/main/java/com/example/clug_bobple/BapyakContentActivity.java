package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BapyakContentActivity extends AppCompatActivity {
    String content_id;
    TextView entrance_year, user_name, date, title, content, comments_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bapyak_content);

        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");

        entrance_year = findViewById(R.id.content_entrance);
        user_name = findViewById(R.id.content_username);
        date = findViewById(R.id.content_date);
        title = findViewById(R.id.content_title);
        content = findViewById(R.id.content_content);
        comments_num = findViewById(R.id.comments_num);

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

        /*String url = getString(R.string.url) + "/bapyak-comment/"+content_id;
        JSONObject postJson = new JSONObject();
        try{
            // 댓글 내용, token,
            //postJson.put("content", );
        } catch (JSONException e){
            e.printStackTrace();
        }*/
    }
}