package com.androprogrammer.socialsignin.listeners;

import com.androprogrammer.socialsignin.model.UserLoginDetails;

/**
 * Created by Wasim on 06-Dec-15.
 */
public interface SocialConnectListener {

	public void onUserConnected(int requestIdentifier,UserLoginDetails userData);
	public void onConnectionError(int requestIdentifier,String message);
	public void onCancelled(int requestIdentifier,String message);
}
