package com.thaihuy.imusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int mId;
    private String mUri;
    private int mUserId;
    private String mGenre;
    private String mTitle;
    private String mStreamUrl;
    private String mArtworkUrl;
    private int mDuration;
    private Boolean mDownloadable;
    private Artist mArtist;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public Boolean getDownloadable() {
        return mDownloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        mDownloadable = downloadable;
    }

    public Artist getArtist() {
        return mArtist;
    }

    public void setArtist(Artist artist) {
        mArtist = artist;
    }

    public static Creator<Song> getCREATOR() {
        return CREATOR;
    }

    public Song() {
    }

    protected Song(Parcel in) {
        mId = in.readInt();
        mUri = in.readString();
        mUserId = in.readInt();
        mGenre = in.readString();
        mTitle = in.readString();
        mStreamUrl = in.readString();
        mArtworkUrl = in.readString();
        mDuration = in.readInt();
        byte tmpMDownloadable = in.readByte();
        mDownloadable = tmpMDownloadable == 0 ? null : tmpMDownloadable == 1;
        mArtist = in.readParcelable(Artist.class.getClassLoader());
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mUri);
        dest.writeInt(mUserId);
        dest.writeString(mGenre);
        dest.writeString(mTitle);
        dest.writeString(mStreamUrl);
        dest.writeString(mArtworkUrl);
        dest.writeInt(mDuration);
        dest.writeByte((byte) (mDownloadable == null ? 0 : mDownloadable ? 1 : 2));
        dest.writeParcelable(mArtist, flags);
    }

    public class SongEntry {
        public static final String ID = "id";
        public static final String URI = "uri";
        public static final String TITLE = "title";
        public static final String STREAM_URL = "stream_url";
        public static final String ARTWORK_URL = "artwork_url";
        public static final String GENRE = "genre";
    }
}
