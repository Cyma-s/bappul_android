package com.example.clug_bobple;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {
    String url;
    List<Marker> previous_marker = new ArrayList<>();
    GoogleMap mMap;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    int len = 0, cnt = 1, sum = 0;
    ImageView list_button, more_button;
    RecyclerView recyclerView;
    private DrawerLayout home_layout;
    private View navigation;
    String ent_year, drawer_name;
    TextView entrance_drw, name_drw;
    private ImageView menu_button;
    private ConstraintLayout mapp;
    private TextView mapintent;
    private TextView home, my_writing, to_restaurant, promise, logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("Authorization", "");
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        RequestQueue token_queue = Volley.newRequestQueue(HomeActivity.this);

        String profile_url = getString(R.string.url) + "/profile/info";

        final JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, profile_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            ent_year = response.get("entranceYear").toString();
                            drawer_name = response.get("name").toString();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("Authorization", "Bearer " + token);
                return heads;
            }
        };

        token_queue.add(profileRequest);


        mapp = (ConstraintLayout)findViewById(R.id.mapp);
        mapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gmap = new Intent(HomeActivity.this, GMap.class);
                startActivity(intent_gmap);
            }
        });
        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_layout.closeDrawer(navigation);
            }
        });
        my_writing = (TextView) findViewById(R.id.my_writing);
        my_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mypage = new Intent(HomeActivity.this, MyPage.class);
                startActivity(intent_mypage);
            }
        });
        to_restaurant = (TextView) findViewById(R.id.to_restaurant);
        to_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_review = new Intent(HomeActivity.this, GMap.class);
                startActivity(intent_review);
            }
        });
        promise = (TextView) findViewById(R.id.promise);
        promise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_list = new Intent(HomeActivity.this, BapyakListActivity.class);
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
                Intent intent_main = new Intent(HomeActivity.this, MainActivity.class);
                finish();
                startActivity(intent_main);
            }
        });

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        mapintent = (TextView) findViewById(R.id.mapintent);

        mapintent.bringToFront();
        mapintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_map = new Intent(HomeActivity.this, GMap.class);
                //finish();
                startActivity(intent_map);
            }
        });

        ArrayList<Recent> list = new ArrayList<>();
        recyclerView = findViewById(R.id.recent_bapyak);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        HomeAdapter adapter = new HomeAdapter(list);
        recyclerView.setAdapter(adapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        if (recyclerView.getOnFlingListener() == null){
            snapHelper.attachToRecyclerView(recyclerView);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();
            }
        });

        url = getString(R.string.url) + "/bapyak" + "/home";

        jsonRequest(queue, adapter);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        home_layout = (DrawerLayout)findViewById(R.id.home_layout);
        navigation = (View)findViewById(R.id.navigation);
        menu_button = (ImageView)findViewById(R.id.menu_button);
        more_button = (ImageView)findViewById(R.id.more_bapyak);
        name_drw = findViewById(R.id.user_name);
        entrance_drw = findViewById(R.id.user_stdnum);


        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_layout.openDrawer(navigation);
                name_drw.setText(drawer_name);
                entrance_drw.setText(ent_year+"학번");
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BapyakListActivity.class);
                finish();
                startActivity(intent);
            }
        });


        home_layout.setDrawerListener(listener);
        navigation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
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

    public void jsonRequest(RequestQueue queue, HomeAdapter adapter){
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //Toast.makeText(BapyakListActivity.this, posts.toString(), Toast.LENGTH_LONG).show();
                            int obj_len = response.length();
                            for (int i = 0; i < obj_len; i++) {
                                JSONObject object = response.getJSONObject(i);
                                adapter.addItem(new Recent(object.get("title").toString(), object.get("content").toString(), object.get("userName").toString()));
                            }
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(37.50532641259903, 126.95701536932249);
        MarkerOptions marker = new MarkerOptions();
        marker.title("중앙대학교");
        marker.position(location);
        mMap.addMarker(marker);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        showPlaceInformation(location);
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }
    public void showPlaceInformation(LatLng location)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();

        new NRPlaces.Builder()
                .listener(HomeActivity.this)
                .key("AIzaSyCKx9eXDOXbFukMXevz0wTO3wSxyXGoY8A")
                .latlng(location.latitude, location.longitude)
                .radius(500)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute();
    }

    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (System.currentTimeMillis() > backKeyPressedTime + 2500){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500){
            finish();
            Toast.makeText(this, "감사합니다 :)", Toast.LENGTH_LONG).show();
        }
    }
}
