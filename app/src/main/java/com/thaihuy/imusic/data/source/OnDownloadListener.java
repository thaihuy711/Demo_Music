package com.thaihuy.imusic.data.source;

public interface OnDownloadListener {
    void onDownloadSuccess(String message);

    void onDownloadFail();
}
