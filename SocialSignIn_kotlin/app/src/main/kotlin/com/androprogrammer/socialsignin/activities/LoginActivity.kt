package com.androprogrammer.socialsignin.activities


import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import butterknife.Bind
import butterknife.ButterKnife
import com.androprogrammer.socialsignin.R
import com.androprogrammer.socialsignin.listeners.SocialConnectListener
import com.androprogrammer.socialsignin.model.UserLoginDetails
import com.androprogrammer.socialsignin.util.AppConstants
import com.androprogrammer.socialsignin.util.PreferenceManager
import com.androprogrammer.socialsignin.util.Utility
import com.androprogrammer.socialsignin.util.helpers.FacebookLoginHelper
import com.androprogrammer.socialsignin.util.helpers.GooglePlusLoginHelper
import com.androprogrammer.socialsignin.util.helpers.TwitterLoginHelper
import java.util.*
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity(), SocialConnectListener, View.OnClickListener {

    // Google Plus Login
    @Bind(R.id.layout_gplustext)
    lateinit var layout_gplus: LinearLayout
    @Bind(R.id.layout_full_gplus)
    lateinit var layout_full_gplus: LinearLayout

    @Bind(R.id.pb_gplusloading)
    lateinit var pb_gpLoader: ProgressBar

    private var gplusHelper: GooglePlusLoginHelper? = null

    // For FacebookLogin Layout
    @Bind(R.id.layout_fbtext)
    lateinit var layout_fb: LinearLayout
    @Bind(R.id.layout_full_fb)
    lateinit var layout_full_fb: LinearLayout
    @Bind(R.id.pb_fbloading)
    lateinit var pb_fbLoader: ProgressBar

    // Facebook Login
    private var fbHelper: FacebookLoginHelper? = null


    // Twitter login
    @Bind(R.id.layout_full_twitter)
    lateinit var layout_full_twitter : LinearLayout;

    @Bind(R.id.layout_twittertext)
    lateinit var layout_twitter: LinearLayout

    @Bind(R.id.pb_twitterloading)
    lateinit var pb_twitterLoader: ProgressBar

    private var twitterHelper: TwitterLoginHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_login)
        ButterKnife.bind(this@LoginActivity)

        initializeView()

        pb_gpLoader.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN)

        pb_fbLoader.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN)

        pb_twitterLoader.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN)

    }

    fun initializeView()
    {

        thread(start = true, isDaemon = true, name = "initialize"){

            gplusHelper = GooglePlusLoginHelper()
            gplusHelper!!.userCallbackListener = this

            fbHelper = FacebookLoginHelper()
            fbHelper!!.createConnection(this@LoginActivity)
            fbHelper!!.userCallbackListener = this

            twitterHelper = TwitterLoginHelper()
            twitterHelper!!.setUserCallbackListener(this)

            layout_full_gplus.setOnClickListener(this@LoginActivity);

            layout_full_fb.setOnClickListener(this@LoginActivity);
            layout_full_twitter.setOnClickListener(this@LoginActivity);

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == AppConstants.GPLUS_SIGN_IN) {
            gplusHelper!!.onActivityResult(requestCode,resultCode, data)
        }
        else if(requestCode == AppConstants.TWITTER_SIGN_IN)
        {
            twitterHelper!!.onActivityResult(requestCode,resultCode,data)
        }
        else {
            fbHelper!!.callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onClick(v: View?) {

        when(v!!.id)
        {
            R.id.layout_full_fb -> {

                Log.d("onclick", "fbclicked")
                if (Utility.isConnectivityAvailable(this@LoginActivity)) {
                    runOnUiThread {
                        layout_full_fb.setBackgroundColor(resources.getColor(R.color.view_disable))
                        layout_full_fb.isEnabled = false
                        layout_fb.visibility = View.GONE
                        pb_fbLoader.visibility = View.VISIBLE
                    }

                    val permissions = ArrayList<String>()
                    permissions.add("public_profile")
                    permissions.add("email")
                    permissions.add("user_location")

                    fbHelper!!.signIn(AppConstants.FB_SIGN_IN, permissions)

                } else {
                    Utility.showToast(this@LoginActivity, resources.getString(R.string.msg_noInternet))
                }
            }

            R.id.layout_full_gplus -> {

                // clicked on google plus login
                if (Utility.isConnectivityAvailable(this@LoginActivity)) {
                    runOnUiThread {
                        layout_full_gplus.setBackgroundColor(resources.getColor(R.color.view_disable))
                        layout_full_gplus.isEnabled = false
                        layout_gplus.visibility = View.GONE
                        pb_gpLoader.visibility = View.VISIBLE
                    }

                    gplusHelper!!.createConnection(this@LoginActivity)
                    gplusHelper!!.signIn(AppConstants.GPLUS_SIGN_IN)

                } else {

                    Utility.showToast(this@LoginActivity, resources.getString(R.string.msg_noInternet))
                }

            }

            R.id.layout_full_twitter -> {

                // clicked on google plus login
                if (Utility.isConnectivityAvailable(this@LoginActivity)) {
                    runOnUiThread {
                        layout_full_twitter.setBackgroundColor(resources.getColor(R.color.view_disable))
                        layout_full_twitter.isEnabled = false
                        layout_twitter.visibility = View.GONE
                        pb_twitterLoader.visibility = View.VISIBLE
                    }

                    twitterHelper!!.createConnection(this@LoginActivity)
                    twitterHelper!!.signIn(AppConstants.TWITTER_SIGN_IN)

                } else {

                    Utility.showToast(this@LoginActivity, resources.getString(R.string.msg_noInternet))
                }

            }
            else ->
                throw UnsupportedOperationException()
        }


    }

    override fun onUserConnected(requestIdentifier: Int, userData: UserLoginDetails) {

        if (requestIdentifier == AppConstants.GPLUS_SIGN_IN) {
            runOnUiThread {
                layout_full_gplus.setBackgroundColor(resources.getColor(R.color.bg_gplus))
                layout_full_gplus.isEnabled = true
                layout_gplus.visibility = View.VISIBLE
                pb_gpLoader.visibility = View.GONE
                PreferenceManager.preferencePutInteger(resources.getString(R.string.pref_login_identifier),
                        AppConstants.GPLUS_SIGN_IN)
            }
        } else if (requestIdentifier == AppConstants.FB_SIGN_IN) {
            runOnUiThread {
                layout_full_fb.setBackgroundColor(resources.getColor(R.color.bg_fb))
                layout_full_fb.isEnabled = true
                layout_fb.visibility = View.VISIBLE
                pb_fbLoader.visibility = View.GONE
                PreferenceManager.preferencePutInteger(resources.getString(R.string.pref_login_identifier),
                        AppConstants.FB_SIGN_IN)
            }
        }else if (requestIdentifier == AppConstants.TWITTER_SIGN_IN) {
            runOnUiThread {

                layout_full_twitter.setBackgroundColor(resources.getColor(R.color.bg_fb))
                layout_full_twitter.isEnabled = true
                layout_twitter.visibility = View.VISIBLE
                pb_twitterLoader.visibility = View.GONE
                PreferenceManager.preferencePutInteger(resources.getString(R.string.pref_login_identifier),
                        AppConstants.TWITTER_SIGN_IN)

            }
        }

        Utility.showToast(this@LoginActivity, "connected")
        PreferenceManager.preferencePutBoolean(resources.getString(R.string.pref_is_loggedin), true)

        val MainScreen = Intent(this@LoginActivity, MainActivity::class.java)

        val b = Bundle()
        b.putSerializable(resources.getString(R.string.key_userModel), userData)

        MainScreen.putExtra(resources.getString(R.string.key_userBundle), b)

        startActivity(MainScreen)
        overridePendingTransition(R.anim.right_slide_in, 0)
    }

    override fun onConnectionError(requestIdentifier: Int, message: String) {
        Utility.showToast(this@LoginActivity, message)

        if (requestIdentifier == AppConstants.GPLUS_SIGN_IN) {
            runOnUiThread {
                layout_full_gplus.setBackgroundColor(resources.getColor(R.color.bg_gplus))
                layout_full_gplus.isEnabled = true
                layout_gplus.visibility = View.VISIBLE
                pb_gpLoader.visibility = View.GONE
            }
        } else if (requestIdentifier == AppConstants.FB_SIGN_IN) {
            runOnUiThread {
                layout_full_fb.setBackgroundColor(resources.getColor(R.color.bg_fb))
                layout_full_fb.isEnabled = true
                layout_fb.visibility = View.VISIBLE
                pb_fbLoader.visibility = View.GONE
            }
        }
        else if (requestIdentifier == AppConstants.TWITTER_SIGN_IN) {
            runOnUiThread {
                layout_full_twitter.setBackgroundColor(resources.getColor(R.color.bg_fb))
                layout_full_twitter.isEnabled = true
                layout_twitter.visibility = View.VISIBLE
                pb_twitterLoader.visibility = View.GONE
            }
        }

    }


    override fun onCancelled(requestIdentifier: Int, message: String) {
        Utility.showToast(this@LoginActivity, message)
    }
}
