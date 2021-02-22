package com.example.clug_bobple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Comment> comments = new ArrayList<Comment>();

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View commentView = inflater.inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(commentView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment item = comments.get(position);
        holder.setItem(item);
    }

    public void addItem(Comment comment){
        comments.add(comment);
    }

    public Comment getItem(int position){
        return comments.get(position);
    }

    public void setItem(int position, Comment item){
        comments.set(position, item);
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView comment_entrance;
        TextView comment_username;
        TextView comment_date;
        TextView comment_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_entrance = itemView.findViewById(R.id.comment_entrance);
            comment_username = itemView.findViewById(R.id.comment_username);
            comment_date = itemView.findViewById(R.id.comment_date);
            comment_content = itemView.findViewById(R.id.comment_content);

        }

        public void setItem(Comment comment){
            comment_entrance.setText(comment.getEntrance_year());
            comment_username.setText(comment.getUser_name());
            comment_date.setText(comment.getDate());
            comment_content.setText(comment.getComment_content());
        }
    }

    public void clear(){
        int size = comments.size();
        if (size > 0){
            for (int i= 0; i<size; i++){
                comments.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
