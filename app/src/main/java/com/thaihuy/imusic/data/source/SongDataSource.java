package com.thaihuy.imusic.data.source;

import android.content.Context;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import java.util.Collection;

public interface SongDataSource {

    interface RemoteDataSource extends SongDataSource {
        void getSongsByGenre(String genre, RequestDataCallBack<MoreData> callBack);

        void downloadTrack(Context context, String url, String fileName,
                RequestDataCallBack<String> callback);

        void searchSongs(String href, RequestDataCallBack<MoreData> callback);
        void loadMoreDataSongList(String nextHref, RequestDataCallBack<MoreData> callback);
    }

    interface LocalDataSource extends SongDataSource {
        void getSongFromLocal(Context context, RequestDataCallBack<MoreData> callBack);
    }
}
