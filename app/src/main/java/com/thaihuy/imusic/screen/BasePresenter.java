package com.thaihuy.imusic.screen;

public interface BasePresenter<T> {

    void setView(T view);

    void onStart();

    void onStop();

}
