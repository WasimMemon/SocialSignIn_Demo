package com.androprogrammer.socialsignin.util.helpers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.androprogrammer.socialsignin.AppConfig

import com.androprogrammer.socialsignin.listeners.SocialConnectListener
import com.androprogrammer.socialsignin.model.UserLoginDetails
import com.androprogrammer.socialsignin.util.Utility
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.OptionalPendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Scope


/**
 * Created by Wasim on 06-Dec-15.
 */
class GooglePlusLoginHelper : GoogleApiClient.OnConnectionFailedListener {


    private var activity: AppCompatActivity? = null
    private var userData: UserLoginDetails? = null

    //private Person person;
    var userCallbackListener: SocialConnectListener? = null

    private var requestIdentifier = 0

    fun createConnection(mActivity: AppCompatActivity) {

        this.activity = mActivity
        userData = UserLoginDetails()

        if (Utility.checkPlayServices(mActivity)) {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Scope(Scopes.PROFILE)).requestScopes(Scope(Scopes.PLUS_LOGIN)).requestProfile().requestEmail().build()

            if (mGoogleApiClient == null) {
                // [START create_google_api_client]
                // Build GoogleApiClient with access to basic profile
                mGoogleApiClient = GoogleApiClient.Builder(mActivity).enableAutoManage(mActivity, this)
						.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
						.build()

            }
        }
    }

    fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent) {

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        //person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        //		Log.i(TAG, "Gender: " + person.toString());
        handleSignInResult(result)

    }

    // [START signIn]
    fun signIn(identifier: Int) {
        requestIdentifier = identifier
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity!!.startActivityForResult(signInIntent, requestIdentifier)
    }
    // [END signIn]


    fun signOut() {
        if (mGoogleApiClient != null) {

            if (!mGoogleApiClient!!.isConnecting) {
                mGoogleApiClient!!.connect()
            }

            mGoogleApiClient!!.disconnect()

            Utility.showToast(AppConfig.getContext(), "You are logged out successfully")
        }
        /**/
    }


    fun handleSignInResult(result: GoogleSignInResult) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess)
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount

            if (acct != null) {
                userData!!.fullName = acct.displayName

                userData!!.setGoogleID(acct.id)
                userData!!.email = acct.email
                if (acct.photoUrl != null) {
                    userData!!.userImageUri = acct.photoUrl.toString()
                }

                /*if (person != null) {
					String imageURL = person.getImage().getUrl().replace("?sz=50", "?sz=100");
					userData.setUserImageUrl(imageURL);
				}*/

				userData!!.isGplusLogin = true
				userData!!.isSocial = true

				if (userCallbackListener != null) {
                    userCallbackListener!!.onUserConnected(requestIdentifier, userData!!)
                }

            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

        Log.d(TAG, "onConnectionFailed:" + connectionResult)

        if (userCallbackListener != null) {
            userCallbackListener!!.onConnectionError(requestIdentifier, connectionResult.errorMessage)
        }
    }

    companion object {

        /* Client for accessing Google APIs */
        private var mGoogleApiClient: GoogleApiClient? = null

        private val TAG = "GooglePlusLoginHelper"
    }
}
