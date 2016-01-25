package com.androprogrammer.socialsignin.model

import java.io.Serializable

/**
 * Created by Wasim on 04-12-2015.
 */



class UserLoginDetails : Serializable {



    var userID: Int = 0

    private var FbID: String? = null
    private var GoogleID: String? = null
    private var isAndroid: Boolean = false

    var twitterID: String? = null
    var fullName: String? = null
    var lastName: String? = null
    var email: String? = null
    var city: String? = null
    var mobile: String? = null
    var password: String? = null
    var userImageUrl: String? = null
    var userImageUri: String? = null
    var state: String? = null


    var isFbLogin: Boolean = false
    var isGplusLogin: Boolean = false
    var isTwittersLogin: Boolean = false
    var isBasicLogin: Boolean = false
    var isSocial: Boolean = false

    fun setFbID(fbID: String) {
        FbID = fbID
    }

    fun setGoogleID(googleID: String) {
        this.GoogleID = googleID
    }

    fun setIsAndroid(isAndroid: Boolean) {
        this.isAndroid = isAndroid
    }
}
