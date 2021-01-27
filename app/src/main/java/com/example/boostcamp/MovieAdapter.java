package com.example.boostcamp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Context context;
    ArrayList<Movie> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie item = items.get(position);
        holder.setItem(item);
        holder.ratingBar.setRating(item.getUserRating() / 2);
        holder.setOnItemClickListener(listener);
    }

    public void addItem(Movie item) {
        items.add(item);
    }

    public void addItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, pubDate, director, actor;
        ImageView image;
        RatingBar ratingBar;
        OnItemClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            pubDate = itemView.findViewById(R.id.pubDate);
            director = itemView.findViewById(R.id.director);
            actor = itemView.findViewById(R.id.actor);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null)
                    listener.onItemClick(ViewHolder.this, v, position);
            });
        }

        public void setItem(Movie item) {
            Glide.with(context)
                    .load(item.getImage())
                    .into(image);
            title.setText(Html.fromHtml(item.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            pubDate.setText(item.getPubDate());
            director.setText(item.getDirector());
            actor.setText(item.getActor());
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}
