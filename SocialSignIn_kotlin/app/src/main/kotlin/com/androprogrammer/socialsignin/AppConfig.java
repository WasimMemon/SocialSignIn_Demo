package com.androprogrammer.socialsignin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Wasim on 23-11-2015.
 */

public class AppConfig extends Application {

    private static AppConfig appInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();

        try {

            appInstance = this;
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sharedPreferencesEditor = sharedPreferences.edit();
            setContext(getApplicationContext());

            //MultiDex.install(this);

            TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_app_id), getResources().getString(R.string.twitter_secret_key));
            Fabric.with(this, new Twitter(authConfig));

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyDeath()
                    .build());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mctx) {
        mContext = mctx;
    }

    public static AppConfig getAppInstance() {
        if (appInstance == null)
            throw new IllegalStateException("The application is not created yet!");
        return appInstance;
    }

    public static SharedPreferences.Editor getApplicationPreferenceEditor()
    {
        return sharedPreferencesEditor;
    }

    public static SharedPreferences getApplicationPreference()
    {
        return sharedPreferences;
    }

}
