/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */

package org.jboss.jive.unsubscribe.services;

import com.jivesoftware.base.User;

/**
 * Main business logic for Unsubscribe feature
 */
public interface UnsubscribeManager {

	/**
	 * Perform unsubscribe for given user
	 *
	 * @param user
	 */
	public void unsubscribe(User user);

}
