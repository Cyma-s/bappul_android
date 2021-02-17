package com.example.clug_bobple;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewWriteActivity extends AppCompatActivity {

    ImageView review_write_button;
    EditText review_content;
    RatingBar star_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        Intent intent = getIntent();
        String restaurant_name = intent.getStringExtra("restaurant");
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("long");

        review_content = findViewById(R.id.review_content);
        String content = review_content.getText().toString();
        star_bar = findViewById(R.id.star_bar);
        int star_num = star_bar.getNumStars();

        review_write_button = findViewById(R.id.review_write_button);
        review_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject locationJson = new JSONObject();
                try{
                    locationJson.put("lat", lat);
                    locationJson.put("long", lon);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                JSONObject reviewContentJson = new JSONObject();
                try {
                    reviewContentJson.put("commentContent", content);
                    reviewContentJson.put("rating", star_num);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                JSONObject reviewJson = new JSONObject();
                try{
                    reviewJson.put("location", locationJson);
                    reviewJson.put("review", reviewContentJson);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(ReviewWriteActivity.this);
                String url = getString(R.string.url)+"/restaurant/"+restaurant_name+"/review";
                RequestQueue queue1 = Volley.newRequestQueue(ReviewWriteActivity.this);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, reviewJson,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(ReviewWriteActivity.this, UserReviewActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewWriteActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsonObjectRequest);

            }
        });
    }
}