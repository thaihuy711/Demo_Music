package com.thaihuy.imusic.screen.mysong;

import android.content.Context;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.RequestDataCallBack;

public class MySongPresenter implements MySongContract.Presenter {

    MySongContract.View mView;
    SongRepository mSongRepository;

    public MySongPresenter(SongRepository songRepository) {
        mSongRepository = songRepository;
    }

    @Override
    public void getSongFromLocal(Context context) {
        mSongRepository.getSongFromLocal(context, new RequestDataCallBack<MoreData>() {
            @Override
            public void onSuccess(MoreData data) {
                mView.showSongsFromLocal(data);
            }

            @Override
            public void onFail(String e) {
                mView.showNoTracks();
            }
        });
    }

    @Override
    public void setView(MySongContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
