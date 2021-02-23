package com.example.clug_bobple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements OnHomeItemClickListener{
    private ArrayList<Recent> homedata = new ArrayList<Recent>();
    OnHomeItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.home_item, parent, false);
        HomeAdapter.ViewHolder viewholder = new HomeAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recent item = homedata.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return homedata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView home_title;
        TextView home_content;
        TextView home_username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_title = itemView.findViewById(R.id.home_title);
            home_content = itemView.findViewById(R.id.home_content);
            home_username = itemView.findViewById(R.id.home_username);
        }
        public void setItem(Recent recent) {
            home_title.setText(recent.getHome_title());
            home_content.setText(recent.getHome_content());
            home_username.setText(recent.getHome_username());
        }
    }

    public Recent getItem(int position){
        return homedata.get(position);
    }

    public void setItem(int position, Recent item){
        homedata.set(position, item);
    }

    public HomeAdapter(ArrayList<Recent> homedata) {
        this.homedata = homedata;
    }
    public void addItem(Recent recent) {
        homedata.add(recent);
    }

    public void setOnItemClicklistener(OnHomeItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    public ArrayList<Recent> getHomedata() {
        return homedata;
    }

    public void setHomedata(ArrayList<Recent> homedata) {
        this.homedata = homedata;
    }
}
