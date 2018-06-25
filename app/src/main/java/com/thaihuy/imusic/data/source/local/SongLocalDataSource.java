package com.thaihuy.imusic.data.source.local;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.thaihuy.imusic.data.model.Artist;
import com.thaihuy.imusic.data.model.MoreData;
import com.thaihuy.imusic.data.model.Song;
import com.thaihuy.imusic.data.source.RequestDataCallBack;
import com.thaihuy.imusic.data.source.SongDataSource;
import java.util.ArrayList;
import java.util.List;

public class SongLocalDataSource implements SongDataSource.LocalDataSource {

    private static SongLocalDataSource sSongLocalDataSource;
    private static final String ARTWORK_URI = "content://media/external/audio/albumart";

    public static synchronized SongLocalDataSource getInstance() {
        if (sSongLocalDataSource == null) {
            sSongLocalDataSource = new SongLocalDataSource();
        }
        return sSongLocalDataSource;
    }

    @Override
    public void getSongFromLocal(Context context, RequestDataCallBack<MoreData> callBack) {
        MoreData moreData = getData(context);
        if (moreData.getSongList() != null) {
            callBack.onSuccess(moreData);
        } else {
            callBack.onFail(null);
        }
    }

    private MoreData getData(Context context) {
        MoreData moreData = new MoreData();
        List<Song> songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver()
                .query(uri, null, MediaStore.Audio.Media.IS_MUSIC + "!= 0", null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                Uri sArtworkUri = Uri.parse(ARTWORK_URI);
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, id);
                String artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                Song song = new Song();
                song.setId(id);
                song.setUri("");
                song.setUserId(0);
                song.setGenre("");
                song.setTitle(title);
                song.setStreamUrl(path);
                song.setArtworkUrl(albumArtUri.toString());
                song.setDownloadable(false);
                Artist artist1 = new Artist();
                artist1.setAvartarUrl("");
                artist1.setId(0);
                artist1.setUsername(artist);
                song.setArtist(artist1);
                songs.add(song);
                cursor.moveToNext();
            }
            moreData.setSongList(songs);
            cursor.close();
        }
        return moreData;
    }
}
