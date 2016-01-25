package com.androprogrammer.socialsignin.listeners

import com.androprogrammer.socialsignin.model.UserLoginDetails

/**
 * Created by Wasim on 06-Dec-15.
 */
interface SocialConnectListener {

    fun onUserConnected(requestIdentifier: Int, userData: UserLoginDetails)
    fun onConnectionError(requestIdentifier: Int, message: String)
    fun onCancelled(requestIdentifier: Int, message: String)
}
