package com.thaihuy.imusic.screen.player;

import android.content.Context;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.screen.BasePresenter;

public interface MusicContract {
    interface View {

        void downloadSuccess(String message);

        void downloadFail(String message);
        void updateSongList(MoreData moreData);
    }

    interface Presenter extends BasePresenter<View> {
        void downloadSong(Context context, String url, String fileName);
    }
}
