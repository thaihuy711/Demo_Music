package com.thaihuy.imusic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    private String mAvatarUrl;
    private int mId;
    private String mUsername;

    public Artist() {
    }

    public String getAvartarUrl() {
        return mAvatarUrl;
    }

    public void setAvartarUrl(String avartarUrl) {
        mAvatarUrl = avartarUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public static Creator<Artist> getCREATOR() {
        return CREATOR;
    }

    private Artist(Parcel in) {
        mAvatarUrl = in.readString();
        mId = in.readInt();
        mUsername = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAvatarUrl);
        dest.writeInt(mId);
        dest.writeString(mUsername);
    }

    public class ArtistEntry {
        public static final String USER_ID = "id";
        public static final String USER_NAME = "username";
        public static final String AVATAR_URL = "avatar_url";
    }
}
