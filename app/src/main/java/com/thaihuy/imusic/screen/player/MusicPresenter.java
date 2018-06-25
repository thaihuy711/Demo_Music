package com.thaihuy.imusic.screen.player;

import android.content.Context;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.RequestDataCallBack;

public class MusicPresenter implements MusicContract.Presenter {

    private SongRepository mSongRepository;
    private MusicContract.View mView;

    MusicPresenter(SongRepository songRepository) {
        mSongRepository = songRepository;
    }

    @Override
    public void setView(MusicContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void downloadSong(Context context, String url, String fileName) {
        mSongRepository.downloadTrack(context, url, fileName, new RequestDataCallBack<String>() {
            @Override
            public void onSuccess(String message) {
                mView.downloadSuccess(message);
            }
            @Override
            public void onFail(String e) {
                mView.downloadFail(e);
            }
        });
    }
}
