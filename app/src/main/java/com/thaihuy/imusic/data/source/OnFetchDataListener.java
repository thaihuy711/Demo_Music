package com.thaihuy.imusic.data.source;

import org.json.JSONException;

public interface OnFetchDataListener {
    void onSuccess(String data) throws JSONException;

    void onFail(String message);
}
