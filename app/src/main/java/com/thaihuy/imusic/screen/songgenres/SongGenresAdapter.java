package com.thaihuy.imusic.screen.songgenres;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thaihuy.imusic.R;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.screen.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SongGenresAdapter extends BaseRecyclerViewAdapter<SongGenresAdapter.ViewHolder> {

    private List<Song> mSongList = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public SongGenresAdapter(Context context, ItemClickListener itemClickListener) {
        super(context);
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SongGenresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_my_song,
                parent, false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongGenresAdapter.ViewHolder holder, int position) {
        holder.setData(mSongList.get(position));
    }

    public void addData(List<Song> songs) {
        if (songs == null) {
            return;
        }
        mSongList.addAll(songs);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewGenre;
        private TextView mTextViewGenre;
        private TextView mTextViewArtist;
        private Song mSong;

        ViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            mImageViewGenre = itemView.findViewById(R.id.image_artist);
            mTextViewGenre = itemView.findViewById(R.id.text_song);
            mTextViewArtist = itemView.findViewById(R.id.text_artist);
            itemView.setOnClickListener(v -> listener.onItemClicked(getAdapterPosition()));
        }

        void setData(Song song) {
            Glide.with(itemView.getContext())
                    .load(song.getArtworkUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.st)).into(mImageViewGenre);
            mTextViewGenre.setText(song.getTitle());
            mTextViewArtist.setText(song.getArtist().getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public interface ItemClickListener {
        void onItemClicked(int position);
    }
}
