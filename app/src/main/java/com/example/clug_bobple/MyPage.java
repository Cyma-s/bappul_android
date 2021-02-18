package com.example.clug_bobple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPage extends AppCompatActivity {
    private DrawerLayout mypage_layout;
    private ImageView menu_button;
    private TextView edit;
    private TextView user_name;
    private View navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mypage_layout = (DrawerLayout) findViewById(R.id.mypage_layout);
        navigation = (View) findViewById(R.id.navigation);
        menu_button = (ImageView) findViewById(R.id.menu_button);
        edit = (TextView) findViewById(R.id.edit);
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
}