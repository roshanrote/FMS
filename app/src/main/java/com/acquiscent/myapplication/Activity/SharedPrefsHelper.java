package com.acquiscent.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PWD = "password";
    private Context mContext;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public SharedPrefsHelper(Context context) {
        mContext = context;

        if (settings == null || editor == null)
        {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }
    }

    private static final String PREFS_NAME = "MailCastApp";

    public void setIntvalues(String Key, int text) {
        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }

        editor.putString(Key, String.valueOf(text));
        editor.apply();
    }

    public void setLongValues(String Key, long text) {

        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }

        editor.putString(Key, String.valueOf(text));
        editor.apply();
    }

    public void setStringValues(String Key, String text) {

        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }

        editor.putString(Key, text);
        editor.apply();
    }

    public void createLoginSession(String username, String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing username in pref
        editor.putString(KEY_USERNAME, username);
        mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putString("loginContact", username).apply();
        // Storing password in pref
        editor.putString(KEY_PWD, password);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            // Closing all the Activities
            // Add new Flag to start new Activity
            // Staring Login Activity
            mContext.startActivity(new Intent(mContext, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        // Closing all the Activities
        // Add new Flag to start new Activity

        // Staring Login Activity
       /* _context.startActivity(new Intent(_context, NewLoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
    }


    public boolean isLoggedIn() {
        return settings.getBoolean(IS_LOGIN, false);
    }


    public void setBoolianValues(String Key, Boolean text) {

        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }
        editor.putBoolean(Key, text);
        editor.apply();
    }

    public String getStringValue(String Key) {

        String text;
        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        text = settings.getString(Key, "");
        return text;
    }

    public String getStringValueUserKey() {

        String text;

        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        text = settings.getString("user_sl_no", "");
        return text;
    }

    public String getStringValueAppId() {

        String text;

        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        text = settings.getString("AppId", "");
        return text;
    }

    public int getIntValue(String Key) {

        int text;

        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        text = settings.getInt(Key, 0);
        return text;
    }

    public boolean getBoolianValue(String Key) {

        boolean text;

        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        text = settings.getBoolean(Key, false);
        return text;
    }

    public Long getLongValue(String Key) {

        Long text;

        if (settings == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        text = settings.getLong(Key, 0);
        return text;
    }

    public void clearSharedPreference() {

        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }
        editor.clear();
        editor.apply();
    }

    public void removeValue(String Key) {

        if (settings == null || editor == null) {
            settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
        }

        editor.remove(Key);
        editor.apply();
    }


}
