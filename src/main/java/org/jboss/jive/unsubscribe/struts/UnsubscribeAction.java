/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */

package org.jboss.jive.unsubscribe.struts;

import com.jivesoftware.base.User;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.community.action.JiveActionSupport;
import com.jivesoftware.community.action.util.AlwaysAllowAnonymous;
import com.jivesoftware.community.web.struts.SetReferer;
import com.jivesoftware.util.StringUtils;
import org.jboss.jive.unsubscribe.services.UnsubscribeManager;
import org.jboss.jive.unsubscribe.services.UnsubscribeManagerImpl;

/**
 * Unsubscribe action
 */
@SetReferer(false)
@AlwaysAllowAnonymous
public class UnsubscribeAction extends JiveActionSupport {

	private String username;

	private String sh;

	private UnsubscribeManager unsubscribeManager;

	@Override
	public String execute() {
		if (StringUtils.isBlank(username)) {
			return NOTFOUND;
		}

		if (StringUtils.isBlank(sh)) {
			return UNAUTHORIZED;
		}

		if (!UnsubscribeManagerImpl.isSecurityHashValid(username, sh)) {
			return UNAUTHORIZED;
		}

		User user;
		try {
			user = userManager.getUser(username);
		} catch (UserNotFoundException e) {
			return UNAUTHORIZED;
		}

		unsubscribeManager.unsubscribe(user);

		return INPUT;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSh() {
		return sh;
	}

	public void setSh(String sh) {
		this.sh = sh;
	}

	public UnsubscribeManager getUnsubscribeManager() {
		return unsubscribeManager;
	}

	public void setUnsubscribeManager(UnsubscribeManager unsubscribeManager) {
		this.unsubscribeManager = unsubscribeManager;
	}
}
