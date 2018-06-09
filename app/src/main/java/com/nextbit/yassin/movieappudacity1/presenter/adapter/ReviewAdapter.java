package com.nextbit.yassin.movieappudacity1.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nextbit.yassin.movieappudacity1.R;
import com.nextbit.yassin.movieappudacity1.domain.model.Review;

import java.util.List;

/**
 * Created by yassin on 10/16/17.
 */

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Review> reviewList;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView author, content;



        public MyViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.revName);
            content = (TextView) view.findViewById(R.id.revContent);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Review item = getItem(getAdapterPosition()); //get item clicked
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            mContext.startActivity(intent);


            notifyDataSetChanged();

        }
    }


    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Review review = reviewList.get(position);




        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());


    }




    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void updateMovies(List<Review> items) {
        reviewList = items;
        notifyDataSetChanged();
    }

    private Review getItem(int adapterPosition) {
        return reviewList.get(adapterPosition);
    }


}
