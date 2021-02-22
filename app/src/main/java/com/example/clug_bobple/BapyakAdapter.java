package com.example.clug_bobple;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BapyakAdapter extends RecyclerView.Adapter<BapyakAdapter.ViewHolder> implements OnBapyakItemClickListener {
    ArrayList<Bapyak> post = new ArrayList<Bapyak>();
    OnBapyakItemClickListener listener;
    String id;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View bapyakView = inflater.inflate(R.layout.bapyak_item, parent, false);

        return new ViewHolder(bapyakView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bapyak item = post.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return post.size();
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

    public void setOnItemClicklistener(OnBapyakItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView entrance_year;
        TextView name;
        TextView title;
        TextView bapyak_date;
        TextView comments;
        String content_id;

        public ViewHolder(View itemView, final OnBapyakItemClickListener listener) {
            super(itemView);

            entrance_year = itemView.findViewById(R.id.bapyak_entranceyear);
            name = itemView.findViewById(R.id.bapyak_username);
            title = itemView.findViewById(R.id.bapyak_title);
            bapyak_date = itemView.findViewById(R.id.bapyak_date);
            comments = itemView.findViewById(R.id.comments);

            // 이 부분에 itemView setOnclickListener 삽입하면 된다.

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Bapyak bapyak){
            entrance_year.setText(bapyak.getEntrance_year());
            name.setText(bapyak.getUser_name());
            title.setText(bapyak.getTitle());
            bapyak_date.setText(bapyak.getBapyak_date());
            comments.setText(bapyak.getComment_num());
        }
    }

    public void clear(){
        int size = post.size();
        if (size > 0){
            for (int i= 0; i<size; i++){
                post.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}