package com.androprogrammer.socialsignin.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil


object Utility {

    fun checkPlayServices(act: Context): Boolean {

        val activity = act as Activity

        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Log.i("checkPlayServices", "This device is not supported.")
                activity.finish()
            }
            return false
        }
        return true
    }

    fun isConnectivityAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    fun showToast(ctx: Context, Message: String) {
        Toast.makeText(ctx, Message, Toast.LENGTH_LONG).show()
    }

}
