package com.androprogrammer.socialsignin.util.helpers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.androprogrammer.socialsignin.listeners.SocialConnectListener
import com.androprogrammer.socialsignin.model.UserLoginDetails
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.services.AccountService

/**
 * Created by Wasim on 24-Jan-16.
 */
class TwitterLoginHelper {

    var client: TwitterAuthClient? = null
        private set

    private var activity: AppCompatActivity? = null
    private var userData: UserLoginDetails? = null

    //    private Callback<TwitterSession> loginCallback;
    private var userCallbackListener: SocialConnectListener? = null

    private var identifier: Int = 0

    fun createConnection(mActivity: AppCompatActivity) {

        this.activity = mActivity
        userData = UserLoginDetails()
        client = TwitterAuthClient()

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        client!!.onActivityResult(requestCode, resultCode, data)

    }

    fun signIn(mIdentifier: Int) {

        if (client != null) {
            identifier = mIdentifier
            client!!.authorize(activity, object : Callback<TwitterSession>() {
                override fun success(twitterSessionResult: Result<TwitterSession>) {
                    Log.d(TAG, "Logged with twitter")
                    val session = twitterSessionResult.data

                    val ac = Twitter.getApiClient(twitterSessionResult.data).accountService
                    ac.verifyCredentials(true, true, object : Callback<com.twitter.sdk.android.core.models.User>() {
                        override fun success(result: Result<com.twitter.sdk.android.core.models.User>) {

                            userData!!.isSocial = true
                            userData!!.setIsAndroid(true)
                            userData!!.isTwittersLogin = true
                            userData!!.twitterID = session.userId.toString()
                            userData!!.fullName = result.data.name
                            //userData.setEmail(result.data.email);
                            userData!!.email = getUserEmail(session)
                            userData!!.userImageUrl = result.data.profileImageUrl

                            if (userCallbackListener != null) {
                                userCallbackListener!!.onUserConnected(identifier, userData!!)
                            }
                        }

                        override fun failure(e: TwitterException) {

                            if (userCallbackListener != null) {
                                userCallbackListener!!.onConnectionError(identifier, e.message.toString())
                            }

                        }
                    })

                }

                override fun failure(e: TwitterException) {
                    Log.e(TAG, "Failed login with twitter")
                    e.printStackTrace()
                    if (userCallbackListener != null) {
                        userCallbackListener!!.onConnectionError(identifier, e.message.toString())
                    }
                }
            })
        }

    }

    fun signOut() {
        Twitter.getSessionManager().clearActiveSession()
    }

    fun setUserCallbackListener(userCallbackListener: SocialConnectListener) {
        this.userCallbackListener = userCallbackListener
    }

    private fun getUserEmail(session: TwitterSession): String {
        var userEmail = ""

        client!!.requestEmail(session, object : Callback<String>() {
            override fun success(result: Result<String>) {
                // Do something with the result, which provides the email address
                Log.d(TAG, "Email found - " + result.toString())
                userEmail = result.data
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
                Log.d(TAG, "Email not found - " + exception.message)
                userEmail = ""
            }
        })

        return userEmail
    }

    companion object {
        private val TAG = "TwitterLoginHelper"
    }
}
