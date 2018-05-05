package com.gboxapps.tmdbtest.adapters;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gboxapps.tmdbtest.R;
import com.gboxapps.tmdbtest.model.Movie;
import com.gboxapps.tmdbtest.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(int position, Movie item, View view, ImageView poster);
    }

    private List<Movie> mDataset;
    private Activity act;
    private OnItemClickListener listener;

    public MovieAdapter(List<Movie> mDataset, Activity act, OnItemClickListener listener) {
        this.mDataset = mDataset;
        this.act = act;
        this.listener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder holder, final int position) {
        holder.setData(mDataset.get(position));

        ViewCompat.setTransitionName(holder.mPoster, mDataset.get(position).getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition(), mDataset.get(position), holder.mParentView, holder.mPoster);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_node)
        View mParentView;
        @BindView(R.id.poster)
        ImageView mPoster;
        @BindView(R.id.title)
        TextView mTitle;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void setData(final Movie movie) {

            mTitle.setText(movie.getTitle());

            Picasso.with(mParentView.getContext())
                    .load(Constants.BASE_URL_IMAGES + movie.getPoster_path())
                    .placeholder(R.drawable.movie_placeholder)
                    .into( mPoster, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                        }
                    });

        }
    }
}
