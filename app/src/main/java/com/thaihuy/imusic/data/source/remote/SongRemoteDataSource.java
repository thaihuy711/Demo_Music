package com.thaihuy.imusic.data.source.remote;

import android.content.Context;
import com.thaihuy.imusic.data.model.Artist;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.source.OnDownloadListener;
import com.thaihuy.imusic.data.source.OnFetchDataListener;
import com.thaihuy.imusic.data.source.RequestDataCallBack;
import com.thaihuy.imusic.data.source.SongDataSource;
import com.thaihuy.imusic.utils.Constant;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SongRemoteDataSource implements SongDataSource.RemoteDataSource {

    private static SongRemoteDataSource sSongRemoteDataSource;

    public static synchronized SongRemoteDataSource getInstance() {
        if (sSongRemoteDataSource == null) {
            sSongRemoteDataSource = new SongRemoteDataSource();
        }
        return sSongRemoteDataSource;
    }

    private MoreData parseJSON(String jsonString) throws JSONException {

        MoreData moreData = new MoreData();
        List<Song> songList = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject1.getJSONArray(Constant.COLLECTION);
        int count = jsonArray.length();

        for (int i = 0; i < count; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Song song = new Song();
            song.setId(jsonObject.getInt(Song.SongEntry.ID));
            song.setArtworkUrl(jsonObject.getString(Song.SongEntry.ARTWORK_URL));
            song.setGenre(jsonObject.getString(Song.SongEntry.GENRE));
            song.setStreamUrl(jsonObject.getString(Song.SongEntry.STREAM_URL));
            song.setUri(jsonObject.getString(Song.SongEntry.URI));
            song.setTitle(jsonObject.getString(Song.SongEntry.TITLE));

            JSONObject artistJsonObject = jsonObject.getJSONObject(Constant.USER);
            Artist artist = new Artist();
            artist.setId(artistJsonObject.getInt(Artist.ArtistEntry.USER_ID));
            artist.setAvartarUrl(artistJsonObject.getString(Artist.ArtistEntry.AVATAR_URL));
            artist.setUsername(artistJsonObject.getString(Artist.ArtistEntry.USER_NAME));
            song.setArtist(artist);
            songList.add(song);
        }
        String nextHref = jsonObject1.getString(Constant.NEXT_HREF);
        moreData.setSongList(songList);
        moreData.setNextHref(nextHref);
        return moreData;
    }

    @Override
    public void getSongsByGenre(String genre, final RequestDataCallBack<MoreData> callBack) {
        new FetchDataFromUrl(new OnFetchDataListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                MoreData moreData = parseJSON(data);
                callBack.onSuccess(moreData);
            }

            @Override
            public void onFail(String e) {
                callBack.onFail(Constant.ERROR_NO_DATA);
            }
        }).execute(Constant.GENRES_URL + genre);
    }

    @Override
    public void downloadTrack(Context context, String url, String fileName,
            final RequestDataCallBack<String> callback) {
        new DownloadFileFromUrl(context, new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String message) {
                callback.onSuccess(message);
            }

            @Override
            public void onDownloadFail() {
                callback.onFail(null);
            }
        }).startDownload(context, url, fileName);
    }

    @Override
    public void searchSongs(String href, RequestDataCallBack<MoreData> callback) {
        new FetchDataFromUrl(new OnFetchDataListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                MoreData moreData = parseJSON(data);
                callback.onSuccess(moreData);
            }

            @Override
            public void onFail(String message) {
                callback.onFail(message);
            }
        }).execute(href);
    }

    @Override
    public void loadMoreDataSongList(String nextHref, RequestDataCallBack<MoreData> callback) {
        new FetchDataFromUrl(new OnFetchDataListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                MoreData moreData = parseJSON(data);
                callback.onSuccess(moreData);
            }

            @Override
            public void onFail(String message) {
                callback.onFail(message);
            }
        }).execute(nextHref);
    }
}
