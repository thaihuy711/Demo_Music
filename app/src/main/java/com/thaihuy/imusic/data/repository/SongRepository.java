package com.thaihuy.imusic.data.repository;

import android.content.Context;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.source.RequestDataCallBack;
import com.thaihuy.imusic.data.source.SongDataSource;
import com.thaihuy.imusic.data.source.local.SongLocalDataSource;
import com.thaihuy.imusic.data.source.remote.SongRemoteDataSource;

public final class SongRepository
        implements SongDataSource.RemoteDataSource, SongDataSource.LocalDataSource {

    private static SongRepository sInstance;
    private SongRemoteDataSource mSongRemoteDataSource;
    private SongLocalDataSource mSongLocalDataSource;

    private SongRepository(SongRemoteDataSource songRemoteDataSource,
            SongLocalDataSource songLocalDataSource) {
        mSongRemoteDataSource = songRemoteDataSource;
        mSongLocalDataSource = songLocalDataSource;
    }

    public static synchronized SongRepository getInstance(SongRemoteDataSource songRemoteDataSource,
            SongLocalDataSource songLocalDataSource) {
        if (sInstance == null) {
            sInstance = new SongRepository(songRemoteDataSource, songLocalDataSource);
        }
        return sInstance;
    }

    @Override
    public void getSongsByGenre(String genre, RequestDataCallBack<MoreData> callBack) {
        mSongRemoteDataSource = SongRemoteDataSource.getInstance();
        mSongRemoteDataSource.getSongsByGenre(genre, callBack);
    }

    @Override
    public void downloadTrack(Context context, String url, String fileName,
            RequestDataCallBack<String> callback) {
        mSongRemoteDataSource.downloadTrack(context,url,fileName,callback);
    }

    @Override
    public void searchSongs(String href, RequestDataCallBack<MoreData> callback) {
       mSongRemoteDataSource.searchSongs(href,callback);
    }

    @Override
    public void loadMoreDataSongList(String nextHref, RequestDataCallBack<MoreData> callback) {
        mSongRemoteDataSource.loadMoreDataSongList(nextHref, callback);
    }

    @Override
    public void getSongFromLocal(Context context, RequestDataCallBack<MoreData> callBack) {
        mSongLocalDataSource.getSongFromLocal(context, callBack);
    }
}
