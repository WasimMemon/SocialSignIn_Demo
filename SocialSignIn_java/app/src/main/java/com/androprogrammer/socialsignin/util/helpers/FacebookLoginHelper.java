package com.androprogrammer.socialsignin.util.helpers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androprogrammer.socialsignin.listeners.SocialConnectListener;
import com.androprogrammer.socialsignin.model.UserLoginDetails;
import com.androprogrammer.socialsignin.util.Utility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Wasim on 13-Dec-15.
 */
public class FacebookLoginHelper {

	private UserLoginDetails userData;

	private AppCompatActivity mActivity;
	private CallbackManager callbackManager;
	private int requestIdentifier = 0;

	private SocialConnectListener userCallbackListener;

	private static final String TAG = "FacebookLoginHelper";


	public FacebookLoginHelper(AppCompatActivity activity) {
		this.mActivity = activity;

		FacebookSdk.sdkInitialize(mActivity);
		callbackManager = CallbackManager.Factory.create();

		userData = new UserLoginDetails();
	}

	public void signIn(int identifier,ArrayList<String> permissions)
	{
		requestIdentifier = identifier;
		LoginManager.getInstance().logInWithReadPermissions(mActivity,permissions);

		startFacebookLogin();

	}

	public void signOut()
	{
		LoginManager.getInstance().logOut();
		Utility.showToast(mActivity, "You are logged out successfully");
	}

	public SocialConnectListener getUserCallbackListener() {
		return userCallbackListener;
	}

	public void setUserCallbackListener(SocialConnectListener userCallbackListener) {
		this.userCallbackListener = userCallbackListener;
	}

	public CallbackManager getCallbackManager() {
		return callbackManager;
	}

	protected void startFacebookLogin() {
		final Bundle parameters = new Bundle();
		parameters.putString("fields", "id,first_name,last_name,email,location");

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						//loginResult.

						final AccessToken accessToken = loginResult.getAccessToken();
						GraphRequest request = GraphRequest.newMeRequest(accessToken,
								new GraphRequest.GraphJSONObjectCallback() {
									@Override
									public void onCompleted(JSONObject user, GraphResponse graphResponse) {
										try {
											Log.d(TAG, user.toString());
											Log.d(TAG, graphResponse.toString());
											userData.setFbID(user.optString("id"));
											userData.setEmail(user.optString("email"));
											userData.setFullName(user.optString("first_name") + " " + user.optString("last_name"));
											//fbUser.setCity(user.getJSONObject("location").getString("name"));
											userData.setIsFbLogin(true);
											userData.setIsSocial(true);
											userData.setIsAndroid(true);
											userData.setUserImageUrl("https://graph.facebook.com/" + user.optString("id") + "/picture?type=normal");
											if (getUserCallbackListener() != null)
											{
												userCallbackListener.onUserConnected(requestIdentifier,userData);
											}
											//onLoginCompleted();

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});

						request.setParameters(parameters);
						request.executeAsync();
					}

					@Override
					public void onCancel() {
						// login cancelled

						if (getUserCallbackListener() != null)
						{
							userCallbackListener.onCancelled(requestIdentifier,"Facebook Login request cancelled...");
						}

					}

					@Override
					public void onError(FacebookException exception) {
						// login error
						if (getUserCallbackListener() != null)
						{
							userCallbackListener.onConnectionError(requestIdentifier,exception.getMessage());
						}
					}
				});
	}

}
