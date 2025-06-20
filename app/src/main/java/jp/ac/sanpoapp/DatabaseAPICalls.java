package jp.ac.sanpoapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DatabaseAPICalls {

    public interface UsersCallback {
        void onSuccess(ArrayList<RemoteAPI.User> users);
        void onError(String errorMessage);
    }

    public void fetchUsers(UsersCallback callback) {
        RemoteAPI api = new RemoteAPI();
        api.getUsers(new RemoteAPI.OnDataReceivedListener() {
            @Override
            public void onDataReceived(ArrayList<RemoteAPI.User> users) {
                // Pass data back to caller
                callback.onSuccess(users);
            }

            @Override
            public void onError(String errorMessage) {
                // Pass error back to caller
                callback.onError(errorMessage);
            }
        });
    }
}
