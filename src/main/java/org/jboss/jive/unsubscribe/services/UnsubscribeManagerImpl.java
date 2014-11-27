/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */

package org.jboss.jive.unsubscribe.services;

import java.util.List;

import com.jivesoftware.base.User;
import com.jivesoftware.community.JiveGlobals;
import com.jivesoftware.community.eae.mail.EmailFrequency;
import com.jivesoftware.community.eae.mail.NotificationSettingsManager;
import com.jivesoftware.community.eae.mail.dao.NotificationSettingsBean;
import com.jivesoftware.community.eae.mail.engagement.DigestFrequency;
import com.jivesoftware.community.eae.streams.StreamManager;
import com.jivesoftware.community.eae.streams.StreamNameTooLongException;
import com.jivesoftware.community.user.preferences.util.EmailFrequencyOption;
import com.jivesoftware.eae.service.client.StreamNameNotUniqueException;
import com.jivesoftware.eae.service.client.api.StreamConfiguration;
import com.jivesoftware.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * UnsubscribeManager implementation
 */
public class UnsubscribeManagerImpl implements UnsubscribeManager {

	private static final Logger log = LogManager.getLogger(UnsubscribeManagerImpl.class);

	public static final String SECURITY_HASH_SALT_KEY = "jboss.unsubscribe.securitykey";

	private NotificationSettingsManager notificationSettingsManager;

	private StreamManager streamManager;

	/**
	 * Get Security Hash for given username and
	 * salt taken from Jive property looked up based on SECURITY_HASH_SALT_KEY key
	 *
	 * @param username
	 * @return security hash or null if username is null or salt is not configured
	 * @see #SECURITY_HASH_SALT_KEY
	 */
	public static String getSecurityHash(String username) {
		String salt = JiveGlobals.getJiveProperty(SECURITY_HASH_SALT_KEY);
		return getSecurityHash(username, salt);
	}

	/**
	 * Get Security Hash for given username and salt
	 *
	 * @param username
	 * @param salt     cannot be null
	 * @return security hash or null if salt or username is configured
	 */
	public static String getSecurityHash(String username, String salt) {
		if (StringUtils.isBlank(username)) {
			return null;
		}

		if (StringUtils.isBlank(salt)) {
			log.warn("Security salt is not configured");
			return null;
		}


		return DigestUtils.sha256Hex(username + salt);
	}

	public static boolean isSecurityHashValid(String username, String sh) {
		String computedSH = UnsubscribeManagerImpl.getSecurityHash(username);
		if (sh == null || computedSH == null) {
			return false;
		}
		return sh.equals(computedSH);
	}

	@Override
	public void unsubscribe(User user) {
		if (log.isInfoEnabled()) {
			log.info("Unsubscribe for user: " + user.getUsername());
		}
		// see com.jivesoftware.community.user.preferences.action.UserPreferencesAction.execute()

		NotificationSettingsBean settings = notificationSettingsManager.getSettings(user);
		settings.setEmailFrequency(EmailFrequency.off);
		settings.setIncludePostContentInEmails(false);
		settings.setNotifyDirectActions(false);
		settings.setNotifyInboxNotifications(false);
		settings.setNotifyModerationQueue(false);
		settings.setReceiveEmails(false);
		settings.setReceiveHTMLEmails(false);

		EmailFrequencyOption.setDigestEmailFrequencyProp(user, DigestFrequency.never);

		settings.setUpgraded(true);

		List<StreamConfiguration> streams = streamManager.getUserStreams(user);
		for (StreamConfiguration config : streams) {
			config.setReceiveEmails(false);

			try {
				streamManager.modifyStream(config);
			} catch (StreamNameNotUniqueException ex) {
				log.error("Cannot unsubscribe from stream", ex);
			} catch (StreamNameTooLongException ex) {
				log.error("Cannot unsubscribe from stream, Stream Name too long", ex);
			}
		}


		notificationSettingsManager.saveSettings(user, settings);
	}

	public void setNotificationSettingsManager(NotificationSettingsManager notificationSettingsManager) {
		this.notificationSettingsManager = notificationSettingsManager;
	}

	public void setStreamManager(StreamManager streamManager) {
		this.streamManager = streamManager;
	}
}
