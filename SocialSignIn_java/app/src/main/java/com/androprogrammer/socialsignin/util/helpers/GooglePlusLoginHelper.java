package com.androprogrammer.socialsignin.util.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androprogrammer.socialsignin.listeners.SocialConnectListener;
import com.androprogrammer.socialsignin.model.UserLoginDetails;
import com.androprogrammer.socialsignin.util.Utility;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;



/**
 * Created by Wasim on 06-Dec-15.
 */
public class GooglePlusLoginHelper implements GoogleApiClient.OnConnectionFailedListener {


	private AppCompatActivity activity;
	private UserLoginDetails userData;

	//private Person person;
	private SocialConnectListener userCallbackListener;

	/* Client for accessing Google APIs */
	private static GoogleApiClient mGoogleApiClient;

	private int requestIdentifier = 0;

	private static final String TAG = "GooglePlusLoginHelper";

	public void createConnection(AppCompatActivity mActivity)
	{

		this.activity = mActivity;
		userData = new UserLoginDetails();

		if (Utility.checkPlayServices(mActivity)) {

			GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestScopes(new Scope(Scopes.PROFILE))
					.requestScopes(new Scope(Scopes.PLUS_LOGIN))
					.requestProfile()
					.requestEmail()
					.build();

			if (mGoogleApiClient == null) {
				// [START create_google_api_client]
				// Build GoogleApiClient with access to basic profile
				mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
						.enableAutoManage(mActivity,this)
						.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
						//.addApi(Plus.API)
						.build();
			}
		}
	}

	public void onActivityResult(int resultCode,Intent data)
	{

		GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
		//person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//		Log.i(TAG, "Gender: " + person.toString());
		handleSignInResult(result);

	}

	// [START signIn]
	public void signIn(int identifier) {
		requestIdentifier = identifier;
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		activity.startActivityForResult(signInIntent, requestIdentifier);
	}
	// [END signIn]


	public void signOut() {
		if (mGoogleApiClient != null) {

			if(!mGoogleApiClient.isConnecting()){
				mGoogleApiClient.connect();
			}

			/*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
					new ResultCallback<Status>() {
						@Override
						public void onResult(Status status) {
							// [START_EXCLUDE]
							if (status.isSuccess())
							{
								Utility.showToast(activity,"You are logged out successfully");
							}
							// [END_EXCLUDE]
						}
					});*/

			mGoogleApiClient.disconnect();

			Utility.showToast(activity, "You are logged out successfully");
		}
		/**/
	}


	public void connectWithGPlus()
	{
		/*if(!mGoogleApiClient.isConnecting()){
			mShouldResolve = true;
			mGoogleApiClient.connect();
		}*/

		OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		if (opr.isDone()) {
			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
			// and the GoogleSignInResult will be available instantly.
			Log.d(TAG, "Got cached sign-in");
			GoogleSignInResult result = opr.get();
			handleSignInResult(result);
		} else {
			// If the user has not previously signed in on this device or the sign-in has expired,
			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
			// single sign-on will occur in this branch.

			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(GoogleSignInResult googleSignInResult) {
			//		hideProgressDialog();
					handleSignInResult(googleSignInResult);
				}
			});
		}

	}

	public SocialConnectListener getUserCallbackListener() {
		return userCallbackListener;
	}

	public void setUserCallbackListener(SocialConnectListener userCallbackListener) {
		this.userCallbackListener = userCallbackListener;
	}


	public void handleSignInResult(GoogleSignInResult result) {

		Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();

			if (acct != null) {
				userData.setFullName(acct.getDisplayName());

				userData.setGoogleID(acct.getId());
				userData.setEmail(acct.getEmail());
				if (acct.getPhotoUrl() != null) {
					userData.setUserImageUri(acct.getPhotoUrl().toString());
				}

				/*if (person != null) {
					String imageURL = person.getImage().getUrl().replace("?sz=50", "?sz=100");
					userData.setUserImageUrl(imageURL);
				}*/

				userData.setIsGplusLogin(true);
				userData.setIsSocial(true);

				if (userCallbackListener != null) {
					userCallbackListener.onUserConnected(requestIdentifier,userData);
				}

			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		Log.d(TAG, "onConnectionFailed:" + connectionResult);

		/*if (!mIsResolving && mShouldResolve) {
			if (connectionResult.hasResolution()) {
				try {
					connectionResult.startResolutionForResult(((LoginActivity)mContext), RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					Log.e(TAG, "Could not resolve ConnectionResult.", e);
					mIsResolving = false;
					mGoogleApiClient.connect();
				}
			} else {
				// Could not resolve the connection result, show the user an
				// error dialog.
				//showErrorDialog(connectionResult);
			}
		}*/

		if (userCallbackListener != null)
		{
			userCallbackListener.onConnectionError(requestIdentifier,connectionResult.getErrorMessage());
		}
	}
}
