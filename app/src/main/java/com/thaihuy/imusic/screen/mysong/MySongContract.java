package com.thaihuy.imusic.screen.mysong;

import android.content.Context;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.screen.BasePresenter;
import java.util.List;

public interface MySongContract {
    interface View {
        void showSongsFromLocal(MoreData moreData);
        void showNoTracks();

    }

    interface Presenter extends BasePresenter<View> {
        void getSongFromLocal(Context context);
    }
}
