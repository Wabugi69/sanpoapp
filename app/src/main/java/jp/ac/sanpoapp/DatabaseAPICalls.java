package jp.ac.sanpoapp;

import java.util.ArrayList;

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
