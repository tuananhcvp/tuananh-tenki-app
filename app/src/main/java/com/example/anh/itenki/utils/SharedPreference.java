package com.example.anh.itenki.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by anh on 2017/12/06.
 */

public class SharedPreference {
    private final String SETTING_NAME = "ienter.co.jp.tenki";
    private static SharedPreference _Instance;// = new SharedPreference();
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mAppEdit = false;

    public synchronized static SharedPreference getInstance(Context context) {
        if(_Instance==null)
            _Instance = new SharedPreference(context);
        return _Instance;
    }

//    public synchronized static SharedPreference getInstance() {
//        if(_Instance == null){
//            _Instance = new SharedPreference();
//        }
//        return _Instance;
//    }

    private SharedPreference(Context context) {
        mPref = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
    }
    public void edit() {
        mAppEdit = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mAppEdit = false;
        mEditor.commit();
        mEditor = null;
    }
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    private void doEdit() {
        if(!mAppEdit && mEditor == null) {
            mEditor = mPref.edit();
        }
    }
    private void doCommit() {
        if(!mAppEdit && mEditor!=null) {
            mEditor.commit();
            mEditor = null;
        }
    }
    //Boolean
    public void putBoolean(String key, boolean value) {
        doEdit();
        mEditor.putBoolean(key, value);
        doCommit();
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        if(mPref!=null) {
            return mPref.getBoolean(key, defaultValue);
        } else {
            return defaultValue;
        }
    }
    //Int
    public void putInt(String key, int value) {
        doEdit();
        mEditor.putInt(key, value);
        doCommit();
    }

    public int getInt(String key, int defaultValue) {
        if(mPref!=null) {
            return mPref.getInt(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    //String
    public void putString(String key, String value) {
        doEdit();
        mEditor.putString(key, value);
        doCommit();
    }
    public String getString(String key, String defaultValue) {
        if(mPref!=null) {
            return mPref.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }
    //Float
    public void putFloat(String key, float value) {
        doEdit();
        mEditor.putFloat(key, value);
        doCommit();
    }

    public float getFloat(String key, float defaultValue) {
        if(mPref!=null) {
            return mPref.getFloat(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    //Double
    public void putDouble(String key, double value) {
        doEdit();
        mEditor.putLong(key, java.lang.Double.doubleToRawLongBits(value));
        doCommit();
    }

    public double getDouble(String key, double defaultValue) {
        if (mPref!=null) {
            return java.lang.Double.longBitsToDouble(mPref.getLong(key, java.lang.Double.doubleToRawLongBits(defaultValue)));
        } else {
            return defaultValue;
        }
    }
}

