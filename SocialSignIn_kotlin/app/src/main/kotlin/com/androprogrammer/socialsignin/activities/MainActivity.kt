package com.androprogrammer.socialsignin.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.androprogrammer.socialsignin.AppConfig
import com.androprogrammer.socialsignin.R
import com.androprogrammer.socialsignin.model.UserLoginDetails
import com.androprogrammer.socialsignin.util.AppConstants
import com.androprogrammer.socialsignin.util.CircleTransform
import com.androprogrammer.socialsignin.util.PreferenceManager
import com.androprogrammer.socialsignin.util.helpers.FacebookLoginHelper
import com.androprogrammer.socialsignin.util.helpers.GooglePlusLoginHelper
import com.androprogrammer.socialsignin.util.helpers.TwitterLoginHelper
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    @Bind(R.id.toolbar)
	lateinit var toolbar: Toolbar
    @Bind(R.id.fab)
	lateinit var fab: FloatingActionButton

    @Bind(R.id.img_userPic)
	lateinit var ivUserPic: ImageView

    @Bind(R.id.tv_userName)
	lateinit var tvUserName: TextView

    @Bind(R.id.tv_userEmail)
	lateinit var tvUserEmail: TextView

    private var userData: UserLoginDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        supportActionBar!!.title = ""

        getIntentData(intent)

		initializeView()

        fab.setOnClickListener {
            if (PreferenceManager.preferenceGetInteger(resources.getString(R.string.pref_login_identifier), 0) == AppConstants.GPLUS_SIGN_IN) {
                val gpHelper = GooglePlusLoginHelper()
                gpHelper.createConnection(this@MainActivity)
                gpHelper.signOut()


            } else if (PreferenceManager.preferenceGetInteger(resources.getString(R.string.pref_login_identifier), 0) == AppConstants.FB_SIGN_IN) {
                val fbHelper = FacebookLoginHelper()
                fbHelper.createConnection(this@MainActivity)
                fbHelper.signOut()
            }
            else if (PreferenceManager.preferenceGetInteger(resources.getString(R.string.pref_login_identifier), 0) == AppConstants.TWITTER_SIGN_IN) {
                val twitterHelper = TwitterLoginHelper()

                twitterHelper.createConnection(this@MainActivity)
                twitterHelper.signOut()
            }

            PreferenceManager.clearPreference()

            val loginView = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(loginView)
            finish()
            overridePendingTransition(0, R.anim.right_slide_out)
        }

    }

    private fun getIntentData(i: Intent?) {

        if (i != null) {
            val b = i.getBundleExtra(resources.getString(R.string.key_userBundle))
            userData = b.getSerializable(resources.getString(R.string.key_userModel)) as UserLoginDetails?
        }
    }


    private fun initializeView() {
        if (userData != null) {
            tvUserName.text = userData!!.fullName
            tvUserEmail.text = userData!!.email

            if (userData!!.userImageUrl != null) {
                Log.d("Image", userData!!.userImageUrl)
                Glide.with(AppConfig.getContext())
						.load(userData!!.userImageUrl)
						.override(100, 100)
						.fitCenter()
						.placeholder(R.mipmap.ic_launcher)
						.transform(CircleTransform(AppConfig.getContext()))
						.into(ivUserPic)
            } else if (userData!!.userImageUri != null) {
                Glide.with(AppConfig.getContext())
						.load(Uri.parse(userData!!.userImageUri))
						.override(100, 100).fitCenter()
						.placeholder(R.mipmap.ic_launcher)
						.transform(CircleTransform(AppConfig.getContext()))
						.into(ivUserPic)
            }
        }
    }

    override fun onBackPressed() {
        overridePendingTransition(0, R.anim.right_slide_out)
        super.onBackPressed()
    }

}
