package com.thaihuy.imusic.screen.songgenres;

import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.screen.BasePresenter;
import java.util.List;

public interface SongGenresContract {
    interface View {
        void showGenres(List<Song> songs);

        //        void showException(Exception e);
        void showError(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void getSongsByGenre(String genre);
    }
}
