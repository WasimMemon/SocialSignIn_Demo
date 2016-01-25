package com.androprogrammer.socialsignin.util.helpers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.androprogrammer.socialsignin.AppConfig
import com.androprogrammer.socialsignin.listeners.SocialConnectListener
import com.androprogrammer.socialsignin.model.UserLoginDetails
import com.androprogrammer.socialsignin.util.Utility
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*

/**
 * Created by Wasim on 13-Dec-15.
 */
class FacebookLoginHelper {

    private var userData: UserLoginDetails? = null

    private var requestIdentifier = 0



	private var mActivity: AppCompatActivity? = null

	var userCallbackListener: SocialConnectListener? = null
	var callbackManager: CallbackManager ? = null

	fun createConnection(activity: AppCompatActivity){

		mActivity = activity
		FacebookSdk.sdkInitialize(mActivity)
		callbackManager = CallbackManager.Factory.create()

		userData = UserLoginDetails()
	}


	fun signIn(identifier: Int, permissions: ArrayList<String>) {
        requestIdentifier = identifier
        LoginManager.getInstance().logInWithReadPermissions(mActivity, permissions)

        startFacebookLogin()

    }

    fun signOut() {
        LoginManager.getInstance().logOut()
        Utility.showToast(AppConfig.getContext(), "You are logged out successfully")
    }


    protected fun startFacebookLogin() {
        val parameters = Bundle()
        parameters.putString("fields", "id,first_name,last_name,email,location")

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        //loginResult.

                        val accessToken = loginResult.accessToken
                        val request = GraphRequest.newMeRequest(accessToken
                        ) { user, graphResponse ->
                            try {
                                Log.d(TAG, user.toString())
                                Log.d(TAG, graphResponse.toString())
                                userData!!.setFbID(user.optString("id"))
                                userData!!.email = user.optString("email")
                                userData!!.fullName = user.optString("first_name") + " " + user.optString("last_name")
                                //fbUser.setCity(user.getJSONObject("location").getString("name"));
								userData!!.isFbLogin = true
								userData!!.isSocial = true
								userData!!.setIsAndroid(true)
                                userData!!.userImageUrl = "https://graph.facebook.com/" + user.optString("id") + "/picture?type=normal"
                                if (userCallbackListener != null) {
                                    userCallbackListener!!.onUserConnected(requestIdentifier, userData!!)
                                }
                                //onLoginCompleted();

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        // login cancelled

                        if (userCallbackListener != null) {
                            userCallbackListener!!.onCancelled(requestIdentifier, "Facebook Login request cancelled...")
                        }

                    }

                    override fun onError(exception: FacebookException) {
                        // login error
                        if (userCallbackListener != null) {
                            userCallbackListener!!.onConnectionError(requestIdentifier, exception.message.toString())
                        }
                    }
                })
    }

    companion object {

        private val TAG = "FacebookLoginHelper"
    }

}
