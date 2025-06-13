package jp.ac.sanpoapp;

import android.content.Context;
import android.content.SharedPreferences;

//Node info and database info
//https://glitch.com/edit/#!/confirmed-sassy-trade?path=server.js%3A24%3A3
//https://console.aiven.io/account/a533724d6c9e/project/catapp/services/catapp-users/databases
public class PrefsManager {
    String username, database, url, user, password, ip, port;

    private static final String SANPOPREFS = "sanpoPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefsManager(Context context){
        sharedPreferences = context.getSharedPreferences(SANPOPREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveToken(String token, String username) {
        editor.putString("token", token);
        editor.putString("username", username);
        editor.apply();  // or commit()
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public void clearToken(){
        editor.putString("token", null);
    }
}
