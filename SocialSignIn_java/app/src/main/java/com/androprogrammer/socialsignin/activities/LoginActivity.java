package com.androprogrammer.socialsignin.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.androprogrammer.socialsignin.R;
import com.androprogrammer.socialsignin.listeners.SocialConnectListener;
import com.androprogrammer.socialsignin.model.UserLoginDetails;
import com.androprogrammer.socialsignin.util.PreferenceManager;
import com.androprogrammer.socialsignin.util.Utility;
import com.androprogrammer.socialsignin.util.helpers.FacebookLoginHelper;
import com.androprogrammer.socialsignin.util.helpers.GooglePlusLoginHelper;
import com.androprogrammer.socialsignin.util.helpers.TwitterLoginHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements SocialConnectListener, View.OnClickListener {

	// Google Plus Login
	@Bind(R.id.layout_gplustext) LinearLayout layout_gplus;
	@Bind(R.id.layout_full_gplus) LinearLayout layout_full_gplus;
	@Bind(R.id.pb_gplusloading) ProgressBar pb_gpLoader;

	/* RequestCode for sign-in */
	private static final int GPLUS_SIGN_IN = 1001;

	private GooglePlusLoginHelper gplusHelper;


	// For FacebookLogin Layout
	@Bind(R.id.layout_fbtext) LinearLayout layout_fb;
	@Bind(R.id.layout_full_fb) LinearLayout layout_full_fb;
	@Bind(R.id.pb_fbloading) ProgressBar pb_fbLoader;

	// Facebook Login
	private FacebookLoginHelper fbHelper;

	private static final int FB_SIGN_IN = 1002;


	// For TwitterLogin Layout
	@Bind(R.id.layout_twittertext) LinearLayout layout_twitter;
	@Bind(R.id.layout_full_twitter) LinearLayout layout_full_twitter;
	@Bind(R.id.pb_twitterloading) ProgressBar pb_twitterLoader;

	// Facebook Login
	private TwitterLoginHelper twitterHelper;

	private static final int TWITTER_SIGN_IN = 140;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		//getHashKey();


		initalizeView();
	}

	private void initalizeView()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {

				gplusHelper = new GooglePlusLoginHelper();
				gplusHelper.setUserCallbackListener(LoginActivity.this);

				pb_gpLoader.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
						PorterDuff.Mode.SRC_IN);

				fbHelper = new FacebookLoginHelper(LoginActivity.this);
				fbHelper.setUserCallbackListener(LoginActivity.this);

				pb_fbLoader.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
						PorterDuff.Mode.SRC_IN);

				twitterHelper = new TwitterLoginHelper();
				twitterHelper.setUserCallbackListener(LoginActivity.this);

				pb_twitterLoader.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),
						PorterDuff.Mode.SRC_IN);

				layout_full_fb.setOnClickListener(LoginActivity.this);
				layout_full_gplus.setOnClickListener(LoginActivity.this);
				layout_full_twitter.setOnClickListener(LoginActivity.this);

			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == GPLUS_SIGN_IN) {
			// If the error resolution was not successful we should not resolve further errors.
			/*if (resultCode != RESULT_OK) {
			}*/

			gplusHelper.onActivityResult(resultCode, data);
		}else if(requestCode == TWITTER_SIGN_IN)
		{
			twitterHelper.onActivityResult(requestCode,resultCode,data);
		}
		else {
			fbHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void onUserConnected(int requestIdentifier,UserLoginDetails userData) {

		if (requestIdentifier == GPLUS_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.bg_gplus));
					layout_full_gplus.setEnabled(true);
					layout_gplus.setVisibility(View.VISIBLE);
					pb_gpLoader.setVisibility(View.GONE);
					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							GPLUS_SIGN_IN);
				}
			});
		} else if (requestIdentifier == FB_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					layout_full_fb.setBackgroundColor(getResources().getColor(R.color.bg_fb));
					layout_full_fb.setEnabled(true);
					layout_fb.setVisibility(View.VISIBLE);
					pb_fbLoader.setVisibility(View.GONE);
					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							FB_SIGN_IN);
				}
			});
		}else if (requestIdentifier == TWITTER_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					layout_full_twitter.setBackgroundColor(getResources().getColor(R.color.bg_fb));
					layout_full_twitter.setEnabled(true);
					layout_twitter.setVisibility(View.VISIBLE);
					pb_twitterLoader.setVisibility(View.GONE);
					PreferenceManager.preferencePutInteger(getResources().getString(R.string.pref_login_identifier),
							TWITTER_SIGN_IN);
				}
			});
		}

		Utility.showToast(LoginActivity.this, "connected");
		PreferenceManager.preferencePutBoolean(getResources().getString(R.string.pref_is_loggedin),true);

		Intent MainScreen = new Intent(LoginActivity.this,MainActivity.class);

		Bundle b = new Bundle();
		b.putSerializable(getResources().getString(R.string.key_userModel),userData);

		MainScreen.putExtra(getResources().getString(R.string.key_userBundle), b);

		startActivity(MainScreen);
		overridePendingTransition(R.anim.right_slide_in, 0);
	}

	@Override
	public void onConnectionError(int requestIdentifier,String message) {
		Utility.showToast(LoginActivity.this, message);

		if (requestIdentifier == GPLUS_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.bg_gplus));
					layout_full_gplus.setEnabled(true);
					layout_gplus.setVisibility(View.VISIBLE);
					pb_gpLoader.setVisibility(View.GONE);
				}
			});
		} else if (requestIdentifier == FB_SIGN_IN) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					layout_full_fb.setBackgroundColor(getResources().getColor(R.color.bg_fb));
					layout_full_fb.setEnabled(true);
					layout_fb.setVisibility(View.VISIBLE);
					pb_fbLoader.setVisibility(View.GONE);
				}
			});
		}

	}


	@Override
	public void onCancelled(int requestIdentifier,String message) {
		Utility.showToast(LoginActivity.this, message);
	}

	public void getHashKey()
	{
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId())
		{

			case R.id.layout_full_fb:

				// clicked on Facebook login
				Log.d("onclick", "fbclicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_full_fb.setBackgroundColor(getResources().getColor(R.color.view_disable));
							layout_full_fb.setEnabled(false);
							layout_fb.setVisibility(View.GONE);
							pb_fbLoader.setVisibility(View.VISIBLE);
						}
					});

					ArrayList<String> permissions = new ArrayList<>();
					permissions.add("public_profile");
					permissions.add("email");
					permissions.add("user_location");

					fbHelper.signIn(FB_SIGN_IN, permissions);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;

			case R.id.layout_full_gplus:

				// clicked on google plus login
				Log.d("onclick", "clicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_full_gplus.setBackgroundColor(getResources().getColor(R.color.view_disable));
							layout_full_gplus.setEnabled(false);
							layout_gplus.setVisibility(View.GONE);
							pb_gpLoader.setVisibility(View.VISIBLE);
						}
					});

					gplusHelper.createConnection(LoginActivity.this);
					gplusHelper.signIn(GPLUS_SIGN_IN);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;

			case R.id.layout_full_twitter:


				Log.d("onclick", "clicked");
				if (Utility.isConnectivityAvailable(LoginActivity.this)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							layout_full_twitter.setBackgroundColor(getResources().getColor(R.color.view_disable));
							layout_full_twitter.setEnabled(false);
							layout_twitter.setVisibility(View.GONE);
							pb_twitterLoader.setVisibility(View.VISIBLE);
						}
					});

					twitterHelper.createConnection(LoginActivity.this);
					twitterHelper.signIn(TWITTER_SIGN_IN);

				} else {
					Utility.showToast(LoginActivity.this,"No internet connection available...");
				}

			break;
		}

	}
}
