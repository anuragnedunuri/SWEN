package com.example.swen.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.swen.CheckNetworkConnectivity;
import com.example.swen.GlobalVariables;
import com.example.swen.NewsData;
import com.example.swen.R;
import com.example.swen.SwenAdapter;
import com.example.swen.SwenLoader;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    /*Variable of type TextView that holds the emptyState view
     *Variable of type ProgressBar that holds the progressBar view
     *Variable of type SwenAdapter for attaching to the list view*/

    //Create a variable for logging
    private final static String LOG_TAG = HomeFragment.class.getName();
    private static SwenAdapter mAdapter;
    private TextView emptyState;
    private ProgressBar progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /* Create a new CheckNetworkConnectivity object for checking the network connection
         * Invoke the method registerNetworkCallback to check for network connection
         */
        CheckNetworkConnectivity connectivity = new CheckNetworkConnectivity(getContext());
        connectivity.registerNetworkCallback();

        View root = inflater.inflate(R.layout.list_item_view, container, false);

        //Fetch the emptyState view
        emptyState = root.findViewById(R.id.emptyState);
        progress = root.findViewById(R.id.progress);


        /* Checks the isNetworkConnected variable in the GlobalVariables class
         * The Variable holds true or false based on the network connectivity
         * as determined in the registerNetworkCallback method in the
         * CheckNetworkConnectivity class. If network is connected, initiate a new
         * loader to call the guardian api and add a debug log. Else,set the emptyState
         * TextView to a message and log a debug message. Also, hide the progressBar view
         * */
        if (GlobalVariables.isNetworkConnected) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
            Log.d("HomeFragment", "Network Connectivity Available");
        } else {
            progress.setVisibility(View.GONE);
            emptyState.setText(R.string.no_internet_connection);
            Log.d("HomeFragment", "Network Connectivity Not Available");
        }

        //Find a reference to the list view in the layout
        ListView swenListView = root.findViewById(R.id.news_list);

        //Set the empty state
        swenListView.setEmptyView(emptyState);

        //Create a new SwenAdapter object
        mAdapter = new SwenAdapter(getContext(), new ArrayList<NewsData>());

        /*Set the adapter to the list view so that
         *the list view can be populated in the UI using the adapter
         */
        swenListView.setAdapter(mAdapter);

        /* Set an on click listener on the ListView, which sends an intent to the web browser
         * to open a website with more information about the selected earthquake
         */
        swenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsData newsItem = mAdapter.getItem(position);

                //Form the Uri object to pass it to the intent
                Uri newsUri = Uri.parse(newsItem.getUrl());

                /*This following code will invoke the browser intent to a
                 *access the URL from a list item
                 */
                Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        return root;
    }

    @NonNull
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(LOG_TAG, "Inside the OnCreateLoader method");
        String guardianURL = "https://content.guardianapis.com/search?q=debates&show-tags=contributor&api-key=a7dab97f-a668-467d-aeb9-01f213ef7a43";
        //Convert the URL into Uri object
        Uri baseUri = Uri.parse(guardianURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new SwenLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> data) {
        //Set the empty state and set the progress visiblity to GONE
        emptyState.setText(R.string.no_news_available);
        progress.setVisibility(View.GONE);

        //Clear the adapter
        mAdapter.clear();

        //If new news data is preset, add to the adapter for display
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsData>> loader) {
        //Clear the adapter on loader reset
        mAdapter.clear();
    }

}
