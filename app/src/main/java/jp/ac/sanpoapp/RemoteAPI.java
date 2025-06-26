package jp.ac.sanpoapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class RemoteAPI {

    private static final String BASE_URL = "https://confirmed-sassy-trade.glitch.me/"; // your endpoint
    private static final String TAG = "RemoteAPI";
    private static int updatedPoints;

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }

//    public void login(String email, String password, String username, AuthCallback callback) {
//        new Thread(() -> {
//            try {
//                URL url = new URL(BASE_URL + "/login");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setDoOutput(true);
//
//                JSONObject json = new JSONObject();
//                json.put("email", email);
//                json.put("password", password);
//                json.put("username", username);
//
//                OutputStream os = conn.getOutputStream();
//                os.write(json.toString().getBytes());
//                os.flush();
//                os.close();
//
//                int responseCode = conn.getResponseCode();
//                InputStream is = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                StringBuilder result = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) result.append(line);
//
//                if (responseCode == 200)
//                    callback.onSuccess(result.toString());
//                else
//                    callback.onError("Login failed: " + result);
//
//            } catch (Exception e) {
//                callback.onError("Network error: " + e.getMessage());
//            }
//        }).start();
//    }

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

    public static String getURL(){
        return BASE_URL;
    }


        public static int updatePoints(Context context, int newPoints) {
            PrefsManager prefs = new PrefsManager(context);
            String token = prefs.getToken();

            new Thread(() -> {
                try {
                    URL url = new URL(BASE_URL + "updatepoints");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Bearer " + token);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    String jsonInputString = String.format("{\"points\": %d}", newPoints);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();
                    InputStream is = (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    br.close();
                    String responseStr = response.toString();
                    Log.d("UpdatePoints", "Raw server response: " + responseStr);

                    if (responseStr.trim().startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        int updatedPoints = jsonObject.getInt("updatedPoints");
                        Log.d("UpdatePoints", "Updated points: " + updatedPoints);
                    } else {
                        Log.e("UpdatePoints", "Response is not valid JSON!");
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());
                    updatedPoints = jsonObject.getInt("updatedPoints");


                    // Toast needs to run on UI thread
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(() ->
                                Toast.makeText(context, "ポイントを更新しました！", Toast.LENGTH_SHORT).show());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(() ->
                                Toast.makeText(context, "エラー: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }
            }).start();
        return updatedPoints;
    }

}
