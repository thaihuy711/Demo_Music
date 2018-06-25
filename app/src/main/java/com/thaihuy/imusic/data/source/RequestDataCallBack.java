package com.thaihuy.imusic.data.source;

public interface RequestDataCallBack<T> {
    void onSuccess(T data);

    void onFail(String e);
}
