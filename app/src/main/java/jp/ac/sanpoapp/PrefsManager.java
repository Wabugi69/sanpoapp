package jp.ac.sanpoapp;

import jp.ac.sanpoapp.RemoteAPI;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrefsManager {


    private static final String SANPOPREFS = "sanpoPrefs";
    private SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    RemoteAPI remoteAPI;

    public PrefsManager(Context context){
        sharedPreferences = context.getSharedPreferences(SANPOPREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        remoteAPI = new RemoteAPI();
    }

    public void saveToken(String token, String username, int points) {
        editor.putString("token", token);
        editor.putString("username", username);
        editor.putInt("points", points);
        editor.apply();  // or commit()
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }
    public String getUsername(){
        return sharedPreferences.getString("username", null);
    }
    public int getPoints(){
        return sharedPreferences.getInt("points", 0);
    }
    public void updateServerPoints(Context context, int points){
            editor.putInt("dbpoints", remoteAPI.updatePoints(context, points));
            editor.apply();
    }
    public void setPoints(int newPoints){
        editor.putInt("points", newPoints);
        editor.apply();
    }

    public void clearToken(){
        editor.remove("token");
        editor.apply();
    }
    public void clearAll(){
        editor.clear();
        editor.apply();
    }

    public void clearLoginData(){
        editor.putString("token", null);
        editor.putString("username", null);
        editor.putString("points", null);
    }

    public HttpURLConnection createSecureConnection(String endpoint, String method) throws IOException {
        URL url = new URL(RemoteAPI.getURL() + endpoint);  // e.g., "/user/profile"
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method); // GET, POST, PUT, DELETE

        // Add token header
        String token = getToken();  // <-- your saved token!
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;

//          Can be accessed with this ( change the link depending on location in the site
//        HttpURLConnection conn = createSecureConnection("/user/profile");
//        conn.connect();
    }
}
