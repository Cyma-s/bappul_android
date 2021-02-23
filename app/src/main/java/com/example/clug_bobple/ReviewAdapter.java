package com.example.clug_bobple;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements OnReviewItemClickListener{
    ArrayList<Review> reviews = new ArrayList<Review>();
    OnReviewItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewView = inflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(reviewView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review item = reviews.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addItem(Review review){
        reviews.add(review);
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public Review getItem(int position){
        return reviews.get(position);
    }

    public void setItem(int position, Review item){
        reviews.set(position, item);
    }

    public void setOnItemClicklistener(OnReviewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position){
        if (listener != null){
            listener.onItemClick(holder, view, position);
        }
    }



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView date;
        TextView content;
        RatingBar star_rate;

        public boolean isEllipsis(TextView textView){
            // 1이 나오면 글씨가 줄여졌다는 뜻
            if (textView.getLayout() != null){
                return textView.getLayout().getEllipsisCount(textView.getLineCount() - 1) > 0;
            }
            return false;
        }

        public ViewHolder(View itemView, final OnReviewItemClickListener listener){
            super(itemView);

            name = itemView.findViewById(R.id.user_name_review);
            date = itemView.findViewById(R.id.review_date);
            content = itemView.findViewById(R.id.review_content);
            star_rate = itemView.findViewById(R.id.star_rate);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        if (isEllipsis(content)){
                            content.setSingleLine(false);
                        } else {
                            content.setSingleLine(true);
                        }
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Review review){
            name.setText(review.getName());
            date.setText(review.getDate());
            content.setText(review.getContent());
            star_rate.setRating(review.getStar_rate());
        }
    }

    public void clear(){
        int size = reviews.size();
        if (size > 0){
            for (int i= 0; i<size; i++){
                reviews.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}
