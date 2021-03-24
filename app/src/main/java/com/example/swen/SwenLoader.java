package com.example.swen;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;


public class SwenLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private static final String LOG_TAG = SwenLoader.class.getName();
    private String apiURL;

    public SwenLoader(Context context, String Url) {
        super(context);
        apiURL = Url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "Inside the OnStartLoadingMethod");
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsData> loadInBackground() {
        Log.d(LOG_TAG, "Inside the LoadInBackground method");
        if (apiURL == null) {
            return null;
        }
        return SwenApiUtils.fetchNews(apiURL);
    }
}
