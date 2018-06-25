package com.thaihuy.imusic.screen.search;

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

public class SongAdapter extends BaseRecyclerViewAdapter<SongAdapter.ViewHolder> {

    private List<Song> mSongs = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    SongAdapter(Context context, ItemClickListener itemClickListener) {
        super(context);
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_my_song, parent, false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        holder.setData(mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    List<Song> getData() {
        return mSongs;
    }

    public void addData(List<Song> songs) {
        mSongs.addAll(songs);
        notifyDataSetChanged();
    }

    void clearData() {
        mSongs.clear();
        notifyDataSetChanged();
    }

    boolean checkData() {
        return mSongs != null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageSong;
        private TextView mTextSongTittle, mTextArtist;

        public ViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            mImageSong = itemView.findViewById(R.id.image_artist);
            mTextSongTittle = itemView.findViewById(R.id.text_song);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            itemView.setOnClickListener(v -> itemClickListener.onItemClicked(getAdapterPosition()));
        }

        void setData(Song song) {
            mTextSongTittle.setText(song.getTitle());
            mTextArtist.setText(song.getArtist().getUsername());
            Glide.with(itemView.getContext())
                    .load(song.getArtworkUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.bg_circle))
                    .into(mImageSong);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(int position);
    }
}
