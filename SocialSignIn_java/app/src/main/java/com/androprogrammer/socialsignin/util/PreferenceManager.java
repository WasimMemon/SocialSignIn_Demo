package com.androprogrammer.socialsignin.util;


import com.androprogrammer.socialsignin.AppConfig;

/**
 * Created by Wasim on 04-12-2015.
 */
public class PreferenceManager {

    /**
     * Application level preference work.
     */
    public static void preferencePutInteger(String key, int value) {
        AppConfig.getApplicationPreferenceEditor().putInt(key, value);
        AppConfig.getApplicationPreferenceEditor().commit();
    }

    public static int preferenceGetInteger(String key, int defaultValue) {
        return AppConfig.getApplicationPreference().getInt(key, defaultValue);
    }

    public static void preferencePutBoolean(String key, boolean value) {
        AppConfig.getApplicationPreferenceEditor().putBoolean(key, value);
        AppConfig.getApplicationPreferenceEditor().commit();
    }

    public static boolean preferenceGetBoolean(String key, boolean defaultValue) {
        return AppConfig.getApplicationPreference().getBoolean(key, defaultValue);
    }

    public static void preferencePutString(String key, String value) {
        AppConfig.getApplicationPreferenceEditor().putString(key, value);
        AppConfig.getApplicationPreferenceEditor().commit();
    }

    public static String preferenceGetString(String key, String defaultValue) {
        return AppConfig.getApplicationPreference().getString(key, defaultValue);
    }

    public static void preferencePutLong(String key, long value) {
        AppConfig.getApplicationPreferenceEditor().putLong(key, value);
        AppConfig.getApplicationPreferenceEditor().commit();
    }

    public static long preferenceGetLong(String key, long defaultValue) {
        return AppConfig.getApplicationPreference().getLong(key, defaultValue);
    }

    public static void preferenceRemoveKey(String key) {
        AppConfig.getApplicationPreferenceEditor().remove(key);
        AppConfig.getApplicationPreferenceEditor().commit();
    }

    public static void clearPreference() {
        AppConfig.getApplicationPreferenceEditor().clear();
        AppConfig.getApplicationPreferenceEditor().commit();
    }
}
