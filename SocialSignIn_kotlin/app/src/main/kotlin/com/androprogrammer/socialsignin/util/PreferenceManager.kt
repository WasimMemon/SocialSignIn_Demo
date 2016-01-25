package com.androprogrammer.socialsignin.util


import com.androprogrammer.socialsignin.AppConfig

/**
 * Created by Wasim on 04-12-2015.
 */
object PreferenceManager {

    /**
     * Application level preference work.
     */
    fun preferencePutInteger(key: String, value: Int) {
        AppConfig.getApplicationPreferenceEditor().putInt(key, value)
        AppConfig.getApplicationPreferenceEditor().commit()
    }

    fun preferenceGetInteger(key: String, defaultValue: Int): Int {
        return AppConfig.getApplicationPreference().getInt(key, defaultValue)
    }

    fun preferencePutBoolean(key: String, value: Boolean) {
        AppConfig.getApplicationPreferenceEditor().putBoolean(key, value)
        AppConfig.getApplicationPreferenceEditor().commit()
    }

    fun preferenceGetBoolean(key: String, defaultValue: Boolean): Boolean {
        return AppConfig.getApplicationPreference().getBoolean(key, defaultValue)
    }

    fun preferencePutString(key: String, value: String) {
        AppConfig.getApplicationPreferenceEditor().putString(key, value)
        AppConfig.getApplicationPreferenceEditor().commit()
    }

    fun preferenceGetString(key: String, defaultValue: String): String {
        return AppConfig.getApplicationPreference().getString(key, defaultValue)
    }

    fun preferencePutLong(key: String, value: Long) {
        AppConfig.getApplicationPreferenceEditor().putLong(key, value)
        AppConfig.getApplicationPreferenceEditor().commit()
    }

    fun preferenceGetLong(key: String, defaultValue: Long): Long {
        return AppConfig.getApplicationPreference().getLong(key, defaultValue)
    }

    fun preferenceRemoveKey(key: String) {
        AppConfig.getApplicationPreferenceEditor().remove(key)
        AppConfig.getApplicationPreferenceEditor().commit()
    }

    fun clearPreference() {
        AppConfig.getApplicationPreferenceEditor().clear()
        AppConfig.getApplicationPreferenceEditor().commit()
    }
}
