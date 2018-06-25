package com.thaihuy.imusic.data.source.remote;

import android.os.AsyncTask;
import com.thaihuy.imusic.data.source.OnFetchDataListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;

public class FetchDataFromUrl extends AsyncTask<String, Void, String> {

    private OnFetchDataListener mOnFetchDataListener;
    private Exception error;

    public FetchDataFromUrl(OnFetchDataListener onFetchDataListener) {
        mOnFetchDataListener = onFetchDataListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        try {
            data = getJSONFromURL(strings[0]);
        } catch (IOException e) {
            error = e;
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            mOnFetchDataListener.onSuccess(s);
        } catch (JSONException e) {
            mOnFetchDataListener.onFail(error.getMessage());
        }
    }

    private String getJSONFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection httpURLConnection;
        URL url = new URL(urlString);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        httpURLConnection.disconnect();
        return sb.toString();
    }
}
