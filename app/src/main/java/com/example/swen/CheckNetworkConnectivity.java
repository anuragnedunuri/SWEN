package com.example.swen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

/*This class has methods that check for network connectivity using the
 * registerNetworkCallback method. The methods in the NetworkCallback interface
 * are overridden. Set the isNetworkConnected static variable from the
 * GlobalVariables class based on network availability */

public class CheckNetworkConnectivity {

    private static final String LOG_TAG = CheckNetworkConnectivity.class.getName();
    //Class variables declared as private. LOG_TAG for logging
    private Context context;

    //Class constructor
    public CheckNetworkConnectivity(Context context) {
        this.context = context;

    }

    /*Method to call the NetworkCallBack interface.Creates a ConnectivityManager instance to fetch the system
     *service. Create a NetworkRequest.Builder object in order to pass it to the registerNetworkCallback method.
     *The onAvailable method indicates that a network is available and sets the isNetworkConnected
     *Static Variable from the GlobalVariables class to true which can be checked in the calling code
     * Catches any exception while checking for network status and sets the isNetworkConnected variable
     * to false.
     */
    public void registerNetworkCallback() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    GlobalVariables.isNetworkConnected = true;
                    Log.d(LOG_TAG, "onAvailable executed");
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    GlobalVariables.isNetworkConnected = false;
                    Log.d(LOG_TAG, "onUnAvailable executed");
                }

                @Override
                public void onLosing(@NonNull Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    Log.d(LOG_TAG, "onLosing executed");
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    GlobalVariables.isNetworkConnected = false;
                    Log.d(LOG_TAG, "onLost executed");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    Log.d(LOG_TAG, "onCapabilitiesChanged executed");
                }

                @Override
                public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
                    super.onBlockedStatusChanged(network, blocked);
                    Log.d(LOG_TAG, "onBlockedStatusChanged executed");
                }

                @Override
                public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties);
                    Log.d(LOG_TAG, "onLinkPropertiesChanged executed");
                }
            });
        } catch (Exception e) {
            GlobalVariables.isNetworkConnected = false;
            Log.e(LOG_TAG, "Exception in registerNetworkCallBack method");
        }
    }
}
