package com.androprogrammer.socialsignin.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androprogrammer.socialsignin.AppConfig;
import com.androprogrammer.socialsignin.R;
import com.androprogrammer.socialsignin.model.UserLoginDetails;
import com.androprogrammer.socialsignin.util.CircleTransform;
import com.androprogrammer.socialsignin.util.PreferenceManager;
import com.androprogrammer.socialsignin.util.helpers.FacebookLoginHelper;
import com.androprogrammer.socialsignin.util.helpers.GooglePlusLoginHelper;
import com.androprogrammer.socialsignin.util.helpers.TwitterLoginHelper;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

	@Bind(R.id.toolbar) Toolbar toolbar;
	@Bind(R.id.fab) FloatingActionButton fab;
	@Bind(R.id.img_userPic) ImageView ivUserPic;
	@Bind(R.id.tv_userName) TextView tvUserName;
	@Bind(R.id.tv_userEmail) TextView tvUserEmail;

	private UserLoginDetails userData;

	/* RequestCode for sign-in */
	private static final int GPLUS_SIGN_IN = 1001;
	private static final int FB_SIGN_IN = 1002;
	private static final int TWITTER_SIGN_IN = 140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle("");

		getIntentData(getIntent());

		initsalizationView();

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (PreferenceManager.preferenceGetInteger(getResources()
						.getString(R.string.pref_login_identifier),0) == GPLUS_SIGN_IN)
				{
					GooglePlusLoginHelper gpHelper = new GooglePlusLoginHelper();
					gpHelper.createConnection(MainActivity.this);
					gpHelper.signOut();


				}
				else if (PreferenceManager.preferenceGetInteger(getResources()
						.getString(R.string.pref_login_identifier),0) == FB_SIGN_IN)
				{
					FacebookLoginHelper fbHelper = new FacebookLoginHelper(MainActivity.this);
					fbHelper.signOut();
				}
				else if (PreferenceManager.preferenceGetInteger(getResources()
						.getString(R.string.pref_login_identifier), 0) == TWITTER_SIGN_IN) {
					TwitterLoginHelper twitterHelper = new TwitterLoginHelper();

					twitterHelper.createConnection(MainActivity.this);
					twitterHelper.signOut();
				}

				PreferenceManager.clearPreference();

				Intent loginView = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(loginView);
				finish();
				overridePendingTransition(0, R.anim.right_slide_out);

			}
		});

	}

	private void getIntentData(Intent i)
	{

		if (i != null)
		{
			Bundle b = i.getBundleExtra(getResources().getString(R.string.key_userBundle));
			userData = (UserLoginDetails) b.getSerializable(getResources().getString(R.string.key_userModel));
		}
	}


	private void initsalizationView()
	{
		if (userData != null)
		{
			tvUserName.setText(userData.getFullName());
			tvUserEmail.setText(userData.getEmail());

			if (userData.getUserImageUrl() != null)
			{
				Log.d("Image", userData.getUserImageUrl());
				Glide.with(AppConfig.getContext())
						.load(userData.getUserImageUrl())
						.override(100, 100)
						.fitCenter()
						.placeholder(R.mipmap.ic_launcher)
						.transform(new CircleTransform(AppConfig.getContext()))
						.into(ivUserPic);
			}
			else if (userData.getUserImageUri() != null)
			{
				Glide.with(AppConfig.getContext())
						.load(Uri.parse(userData.getUserImageUri()))
						.override(100, 100)
						.fitCenter()
						.placeholder(R.mipmap.ic_launcher)
						.transform(new CircleTransform(AppConfig.getContext()))
						.into(ivUserPic);
			}
		}
	}

	@Override
	public void onBackPressed() {
		overridePendingTransition(0, R.anim.right_slide_out);
		super.onBackPressed();
	}
}
