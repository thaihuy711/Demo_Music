package com.thaihuy.imusic.screen.search;

import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.repository.SongRepository;
import com.thaihuy.imusic.data.source.RequestDataCallBack;

public class SearchPresenter implements SearchContract.Presenter {

    private SongRepository mSongRepository;
    private SearchContract.View mView;

    public SearchPresenter(SongRepository songRepository) {
        mSongRepository = songRepository;
    }

    @Override
    public void searchSong(String href) {
        mSongRepository.searchSongs(href, new RequestDataCallBack<MoreData>() {
            @Override
            public void onSuccess(MoreData data) {
                if (data.getSongList() != null) {
                    mView.showSong(data);
                }
            }

            @Override
            public void onFail(String e) {
                mView.showError(e);
            }
        });
    }

    @Override
    public void loadMoreDataSongList(String nextHref) {
        mSongRepository.loadMoreDataSongList(nextHref, new RequestDataCallBack<MoreData>() {
            @Override
            public void onSuccess(MoreData data) {
                mView.updateSongList(data);
            }

            @Override
            public void onFail(String e) {

            }
        });
    }

    @Override
    public void setView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
