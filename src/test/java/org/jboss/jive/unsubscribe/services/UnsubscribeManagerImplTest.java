/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */

package org.jboss.jive.unsubscribe.services;

import org.junit.Assert;
import org.junit.Test;


/**
 * Test for  UnsubscribeManagerImpl
 */
public class UnsubscribeManagerImplTest {
	@Test
	public void testGetSecurityHash() throws Exception {
		Assert.assertNull(UnsubscribeManagerImpl.getSecurityHash("eee"), null);
		Assert.assertNull(UnsubscribeManagerImpl.getSecurityHash(null), null);

		final String salt = "salt";

		Assert.assertEquals("88edec0a6dd26bb457672c59b02a681da9505b737dd10495ed48ae686ab1e418",
				UnsubscribeManagerImpl.getSecurityHash("eee", salt));

	}

}
