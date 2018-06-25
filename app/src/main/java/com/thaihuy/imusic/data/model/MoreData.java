package com.thaihuy.imusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class MoreData implements Parcelable {

    private List<Song> mSongList;
    private String mNextHref;


    public List<Song> getSongList() {
        return mSongList;
    }

    public void setSongList(List<Song> songList) {
        mSongList = songList;
    }

    public String getNextHref() {
        return mNextHref;
    }

    public void setNextHref(String nextHref) {
        mNextHref = nextHref;
    }

    public static Creator<MoreData> getCREATOR() {
        return CREATOR;
    }

    public MoreData() {
    }

    public MoreData(Parcel in) {
        mSongList = in.createTypedArrayList(Song.CREATOR);
        mNextHref = in.readString();
    }

    public static final Creator<MoreData> CREATOR = new Creator<MoreData>() {
        @Override
        public MoreData createFromParcel(Parcel in) {
            return new MoreData(in);
        }

        @Override
        public MoreData[] newArray(int size) {
            return new MoreData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mSongList);
        dest.writeString(mNextHref);
    }
}
