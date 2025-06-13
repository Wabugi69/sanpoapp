package jp.ac.sanpoapp;

import jp.ac.sanpoapp.RemoteAPI;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//Node info and database info
//https://glitch.com/edit/#!/confirmed-sassy-trade?path=server.js%3A24%3A3
//https://console.aiven.io/account/a533724d6c9e/project/catapp/services/catapp-users/databases
public class PrefsManager {
    String database, url, user, password, ip, port;

    private static final String SANPOPREFS = "sanpoPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefsManager(Context context){
        sharedPreferences = context.getSharedPreferences(SANPOPREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

    public void clearToken(){
        editor.putString("token", null);
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
    }
}
