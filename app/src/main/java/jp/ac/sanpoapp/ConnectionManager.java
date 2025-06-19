package jp.ac.sanpoapp;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class ConnectionManager {

    private final ConnectivityManager connectivityManager;

    public ConnectionManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public void performAction(Runnable action) {
        if (hasValidInternetConnection()) {
            action.run();
        }
    }

    private boolean hasValidInternetConnection() {
        if (connectivityManager == null) return false;

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        if (capabilities == null) return false;

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }
}
