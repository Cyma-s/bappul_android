package com.example.clug_bobple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPage extends AppCompatActivity {
    String url;
    int len = 0, cnt = 1, sum = 0;
    private DrawerLayout mypage_layout;
    private ImageView menu_button;
    private TextView edit;
    private TextView user_name;
    private View navigation;
    private TextView home, my_writing, to_restaurant, promise, logout;
    private RecyclerView my_writing_rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        my_writing_rcv = (RecyclerView)findViewById(R.id.my_writing_rcv);
        mypage_layout = (DrawerLayout) findViewById(R.id.mypage_layout);
        navigation = (View) findViewById(R.id.navigation);
        menu_button = (ImageView) findViewById(R.id.menu_button);
        user_name = (TextView) findViewById(R.id.user_name);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypage_layout.openDrawer(navigation);
            }
        });
        mypage_layout.setDrawerListener(listener);
        navigation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent(MyPage.this, HomeActivity.class);
                startActivity(intent_home);
            }
        });
        my_writing = (TextView) findViewById(R.id.my_writing);
        my_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypage_layout.closeDrawer(navigation);
            }
        });
        to_restaurant = (TextView) findViewById(R.id.to_restaurant);
        to_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_review = new Intent(MyPage.this, GMap.class);
                startActivity(intent_review);
            }
        });
        promise = (TextView) findViewById(R.id.promise);
        promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_list = new Intent(MyPage.this, BapyakListActivity.class);
                startActivity(intent_list);
            }
        });
        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Authorization", "");
                editor.apply();
                Intent intent_main = new Intent(MyPage.this, MainActivity.class);
                startActivity(intent_main);
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(MyPage.this);
        url = getString(R.string.url) + "/mypage/my_bapyaks/" + Integer.toString(cnt);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        my_writing_rcv.setLayoutManager(layoutManager);
        BapyakAdapter adapter = new BapyakAdapter();

        adapter.setOnItemClicklistener(new OnBapyakItemClickListener() {
            @Override
            public void onItemClick(BapyakAdapter.ViewHolder holder, View view, int position) {
                Bapyak item = adapter.getItem(position);
                String id = item.getContent_id();
                Intent intent = new Intent(MyPage.this, BapyakContentActivity.class);
                intent.putExtra("content_id", id);
                startActivity(intent);
            }
        });

        jsonRequest(queue, adapter, token);

        morePostLoad(adapter, queue, token);

        my_writing_rcv.setAdapter(adapter);

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
                            String user = response.get("name").toString();
                            user_name.setText(user);

                            Toast.makeText(MyPage.this, Integer.toString(len), Toast.LENGTH_LONG).show();
                            //Toast.makeText(BapyakListActivity.this, posts.toString(), Toast.LENGTH_LONG).show();


                            for (int i = 0; i < len; i++) {
                                if (cnt == 1) {
                                    JSONObject object = posts.getJSONObject(i);
                                    adapter.addItem(new Bapyak(object.get("title").toString(), object.get("userName").toString(),
                                            object.get("userEntranceYear").toString() + "학번", object.get("createdDate").toString(),
                                            object.get("comentNum").toString(), object.get("id").toString()));

                                    if (i == len - 1) cnt += 1;
                                }
                            }

                            sum += len;
                            my_writing_rcv.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyPage.this, "오류입니다.", Toast.LENGTH_SHORT).show();
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

    public void morePostLoad(BapyakAdapter adapter, RequestQueue queue, String token){
        my_writing_rcv.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)){
                    url = getString(R.string.url) + "/mypage/my_bapyaks/" + Integer.toString(cnt);
                    Toast.makeText(MyPage.this, url, Toast.LENGTH_SHORT).show();

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
                                                more_post.get("userEntranceYear").toString() + "학번", more_post.get("createdDate").toString(),
                                                more_post.get("comentNum").toString(), more_post.get("id").toString()));
                                        if (i == len-1) cnt += 1;
                                    }
                                    sum += len;
                                    LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(my_writing_rcv.getContext()){
                                        @Override
                                        protected int getVerticalSnapPreference() {
                                            return LinearSmoothScroller.SNAP_TO_END;
                                        }
                                    };
                                    linearSmoothScroller.setTargetPosition(sum);
                                    my_writing_rcv.getLayoutManager().startSmoothScroll(linearSmoothScroller);
                                    my_writing_rcv.setAdapter(adapter);
                                } catch (JSONException exception) {
                                    exception.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MyPage.this, "마지막 리뷰입니다.", Toast.LENGTH_SHORT).show();
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



    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}