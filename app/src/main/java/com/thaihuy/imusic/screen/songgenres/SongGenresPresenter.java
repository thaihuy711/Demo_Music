package com.thaihuy.imusic.screen.songgenres;

import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.RequestDataCallBack;

public class SongGenresPresenter implements SongGenresContract.Presenter {

    private SongGenresContract.View mView;
    private SongRepository mSongRepository;

    public SongGenresPresenter(SongRepository songRepository) {
        mSongRepository = songRepository;
    }

    @Override
    public void setView(SongGenresContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void getSongsByGenre(String genre) {
        mSongRepository.getSongsByGenre(genre, new RequestDataCallBack<MoreData>() {
            @Override
            public void onSuccess(MoreData data) {
                if (data.getSongList() != null) {
                    mView.showGenres(data.getSongList());
                }
            }

            @Override
            public void onFail(String e) {
                mView.showError(e);
            }
        });
    }
}
