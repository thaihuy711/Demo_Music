package com.thaihuy.imusic.screen.search;

import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.screen.BasePresenter;

public interface SearchContract {
    interface View {
        void showSong(MoreData moreData);

        void showError(String message);

        void updateSongList(MoreData moreData);
    }

    interface Presenter extends BasePresenter<View> {
        void searchSong(String href);

        void loadMoreDataSongList(String nextHref);
    }
}
