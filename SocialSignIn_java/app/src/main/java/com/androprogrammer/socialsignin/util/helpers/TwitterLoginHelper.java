package com.androprogrammer.socialsignin.util.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androprogrammer.socialsignin.listeners.SocialConnectListener;
import com.androprogrammer.socialsignin.model.UserLoginDetails;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

/**
 * Created by Wasim on 24-Jan-16.
 */
public class TwitterLoginHelper {

    private TwitterAuthClient client;

    private AppCompatActivity activity;
    private UserLoginDetails userData;

//    private Callback<TwitterSession> loginCallback;
    private SocialConnectListener userCallbackListener;

    private int identifier;
    private static final String TAG = "TwitterLoginHelper";

    public void createConnection(AppCompatActivity mActivity) {

        this.activity = mActivity;
        userData = new UserLoginDetails();
        client = new TwitterAuthClient();

    }

    public TwitterAuthClient getClient() {
        return client;
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data) {

        client.onActivityResult(requestCode,resultCode,data);

    }

    public void signIn(final int mIdentifier) {

        if (client != null)
        {
            identifier = mIdentifier;
            client.authorize(activity, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    Log.d(TAG, "Logged with twitter");
                    final TwitterSession session = twitterSessionResult.data;

                    AccountService ac = Twitter.getApiClient(twitterSessionResult.data).getAccountService();
                    ac.verifyCredentials(true, true, new Callback<User>() {
                        @Override
                        public void success(Result<User> result) {

                            userData.setIsSocial(true);
                            userData.setIsAndroid(true);
                            userData.setTwittersLogin(true);
                            userData.setTwitterID(String.valueOf(session.getUserId()));
                            userData.setFullName(result.data.name);
                            //userData.setEmail(result.data.email);
                            userData.setEmail(getUserEmail(session));
                            userData.setUserImageUrl(result.data.profileImageUrl);

                            if (userCallbackListener != null)
                            {
                                userCallbackListener.onUserConnected(identifier,userData);
                            }
                        }

                        @Override
                        public void failure(TwitterException e) {

                            if (userCallbackListener != null)
                            {
                                userCallbackListener.onConnectionError(identifier,e.getMessage());
                            }

                        }
                    });

                    /**/


                }

                @Override
                public void failure(TwitterException e) {
                    Log.e(TAG, "Failed login with twitter");
                    e.printStackTrace();
                    if (userCallbackListener != null)
                    {
                        userCallbackListener.onConnectionError(identifier,e.getMessage());
                    }
                }
            });
        }

    }

    public void signOut()
    {
        Twitter.getSessionManager().clearActiveSession();
    }

    public void setUserCallbackListener(SocialConnectListener userCallbackListener) {
        this.userCallbackListener = userCallbackListener;
    }

    private String getUserEmail(TwitterSession session)
    {
        final String[] userEmail = new String[1];

        client.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Log.d(TAG,"Email found - " + result.toString());
                userEmail[0] = result.data;
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d(TAG,"Email not found - " + exception.getMessage());
                userEmail[0] = "";
            }
        });

        return userEmail[0];
    }
}
