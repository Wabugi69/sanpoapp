package jp.ac.sanpoapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RemoteAPI {

    //TODO Get the new pages from ウイン's GIT uploads

    private static final String BASE_URL = "https://confirmed-sassy-trade.glitch.me/users"; // your endpoint
    private static final String TAG = "RemoteAPI";

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public void login(String email, String password, String username, AuthCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("email", email);
                json.put("password", password);
                json.put("username", username);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                InputStream is = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);

                if (responseCode == 200)
                    callback.onSuccess(result.toString());
                else
                    callback.onError("Login failed: " + result);

            } catch (Exception e) {
                callback.onError("Network error: " + e.getMessage());
            }
        }).start();
    }


    public interface OnDataReceivedListener {
        void onDataReceived(ArrayList<User> users);
        void onError(String errorMessage);
    }

    public void getUsers(OnDataReceivedListener listener) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("X-api-key", AppConstants.API_KEY);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoInput(true);

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line.trim());
                }

                Log.d(TAG, "Response: " + response.toString());

                // Parse JSON
                JSONArray jsonArray = new JSONArray(response.toString());
                ArrayList<User> users = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String id = obj.getString("id");
                    String email = obj.getString("email");
                    String username = obj.getString("username");
                    users.add(new User(id, email, username));
                }

                listener.onDataReceived(users);

            } catch (Exception e) {
                Log.d(TAG, "Error: " + e.getLocalizedMessage());
                listener.onError(e.getLocalizedMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public static class User {
        public String id;
        public String email;
        public String username;

        public User(String id, String email, String username) {
            this.id = id;
            this.email = email;
            this.username = username;
        }
    }
}
