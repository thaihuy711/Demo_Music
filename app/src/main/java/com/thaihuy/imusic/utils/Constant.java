package com.thaihuy.imusic.utils;

import android.support.annotation.StringDef;

import com.thaihuy.imusic.BuildConfig;

public final class Constant {

    @StringDef({
            Genres.ALTERNATIVEROCK, Genres.AMBIENT, Genres.CLASSICAL, Genres.COUNTRY, Genres.ROCK
    })
    public @interface Genres {

        String ALTERNATIVEROCK = "alternativerock";
        String AMBIENT = "ambient";
        String CLASSICAL = "classical";
        String COUNTRY = "country";
        String ROCK = "rock";
    }

    public static final String BASE_URL = "http://api.soundcloud.com";
    public static final String CLIENT_ID = "?client_id=" + BuildConfig.API_KEY;
    public static final String GENRES_URL =
            BASE_URL + "/tracks" + CLIENT_ID + "&linked_partitioning=1&genres=";
    public static final String ARTIST_URL =
            BASE_URL + "/users" + CLIENT_ID + "&limit=50&linked_partitioning=1&offset=100";
    public static final String SEARCH_URL =
            BASE_URL + "/tracks" + CLIENT_ID + "&linked_partitioning=1&q=";

    public static final String SHUFFLE = "shuffle";
    public static final String REPEAT = "repeat";
    public static final String REPEAT_ONE = "repeat_one";
    public static final String NON_REPEAT = "non_repeat";
    public static final String SETUP = "setup";
    public static final String SETUP_MUSIC_PREFERENCES = "setup_music_preferences";
    public static final String NEXT_HREF = "next_href";
    public static final String COLLECTION = "collection";
    public static final String USER = "user";
    public static final String TAG_PLAY_FRAGMENT = "tag_play_fragment";
    public static final String POSITION = "position";
    public static final String EXTRAS_SONG = "EXTRAS_SONG";
    public static final String ISLOCALTRACK = "isLocalTrack";

    public static final String ACTION_PLAY = "com.thaihuy.imusic.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.thaihuy.imusic.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.thaihuy.imusic.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.thaihuy.imusic.ACTION_NEXT";
    public static final String ACTION_CLOSE = "com.thaihuy.imusic.ACTION_CLOSE";
    public static final String ERROR_NO_DATA = "Không có dữ liệu";
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Constant() {

    }
}