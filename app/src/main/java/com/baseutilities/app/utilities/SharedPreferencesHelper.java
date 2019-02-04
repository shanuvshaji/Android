package com.baseutilities.app.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferencesHelper {

    private static SharedPreferencesHelper INSTANCE = null;
    Context mContext;

    public SharedPreferencesHelper(Context mContext) {

        if (mContext != null)
            this.mContext = mContext;

    }

    public static synchronized SharedPreferencesHelper getInstance(Context mContext) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPreferencesHelper(mContext);
        }
        return (INSTANCE);
    }


    public void putInt(String key, int value) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void putBoolean(String key, boolean val) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public void putString(String key, String val) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(key, val);
        edit.commit();

    }

    public void remove(String key) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.remove(key);
        edit.commit();

    }

    public void putFloat(String key, float val) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putFloat(key, val);
        edit.commit();
    }

    public void putLong(String key, long val) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putLong(key, val);
        edit.commit();
    }

    public long getLong(String key, long _default) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        return preferences.getLong(key, _default);
    }

    public float getFloat(String key, float _default) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        return preferences.getFloat(key, _default);
    }

    public String getString(String key, String _default) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        return preferences.getString(key, _default);
    }

    public int getInt(String key, int _default) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        return preferences.getInt(key, _default);
    }

    public boolean getBoolean(String key, boolean _default) {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, _default);
    }

    public void clearPreferences() {
        SharedPreferences preferences= mContext.getSharedPreferences("WALKSTARR", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }
}