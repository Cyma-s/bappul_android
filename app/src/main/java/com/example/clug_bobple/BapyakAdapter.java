package com.example.clug_bobple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BapyakAdapter extends RecyclerView.Adapter<BapyakAdapter.ViewHolder> {
    ArrayList<Bapyak> post = new ArrayList<Bapyak>();

    @NonNull
    @Override
    public BapyakAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View bapyakView = inflater.inflate(R.layout.bapyak_item, parent, false);

        return new ViewHolder(bapyakView);
    }

    @Override
    public void onBindViewHolder(@NonNull BapyakAdapter.ViewHolder holder, int position) {
        Bapyak item = post.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addItem(Bapyak bapyak){
        post.add(bapyak);
    }

    public Bapyak getItem(int position) {
        return post.get(position);
    }

    public void setItem(int position, Bapyak item) {
        post.set(position, item);
    }

    public ArrayList<Bapyak> getPost() {
        return post;
    }

    public void setPost(ArrayList<Bapyak> post) {
        this.post = post;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView entrance_year;
        TextView name;
        TextView title;
        TextView bapyak_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entrance_year = itemView.findViewById(R.id.bapyak_entranceyear);
            name = itemView.findViewById(R.id.bapyak_username);
            title = itemView.findViewById(R.id.bapyak_title);
            bapyak_date = itemView.findViewById(R.id.bapyak_date);

            // 이 부분에 itemView setOnclickListener 삽입하면 된다.
        }

        public void setItem(Bapyak bapyak){
            entrance_year.setText(bapyak.getEntrance_year());
            name.setText(bapyak.getUser_name());
            title.setText(bapyak.getTitle());
            bapyak_date.setText(bapyak.getBapyak_date());
        }
    }
}