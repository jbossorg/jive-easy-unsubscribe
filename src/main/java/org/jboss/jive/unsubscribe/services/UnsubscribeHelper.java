/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */

package org.jboss.jive.unsubscribe.services;

import com.jivesoftware.base.User;

/**
 * Helper class for Unsubscribe logic
 */
public class UnsubscribeHelper {

	/**
	 * Creates relative link to unsubscribe page for specified user
	 *
	 * @param user
	 * @return
	 */
	public static String createSecuredUnsubscribeLink(User user) {
		return "/unsubscribe?username=" + user.getUsername() + "&sh=NA";
	}

}
