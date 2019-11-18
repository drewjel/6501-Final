package edu.virginia.cs.mooncake.wada.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    public static String getSharedPref(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public static void putSharedPref(String key, String value, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public static int getSharedPrefInt(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static void putSharedPrefInt(String key, int value, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().putInt(key, value).apply();
    }

    public static Long getSharedPrefLong(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getLong(key, 0);
    }

    public static void putSharedPrefLong(String key, long value, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().putLong(key, value).apply();
    }

    public static boolean getSharedPrefBoolean(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void putSharedPrefBoolean(String key, boolean value,
                                            Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().putBoolean(key, value).apply();
    }

    public static void removeEntry(String key, Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().remove(key).apply();
    }

    public static void removeAll(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                ConstantsUtil.SHARED_PREF, Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }



}
