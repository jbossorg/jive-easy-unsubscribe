Jive Plugin: Easy Unsubscribe
=============================

Provides easy way for users to unsubscribe from Jive e-mail communication

Installation steps
------------------

* Install plugin via Admin console
* Restart Jive application
* Add new Jive System Property with key `jboss.unsubscribe.securitykey` and any value
* Add a link to unsubscribe to message template like Email Digest by this code

		<#assign objectConstructor = "freemarker.template.utility.ObjectConstructor"?new()>
		<#assign unsubscribeHelper = objectConstructor("org.jboss.jive.unsubscribe.services.UnsubscribeHelper")>
		${EmailNotificationHelper.generateTrackableURL(emailTemplateName, jiveURL + unsubscribeHelper.createSecuredUnsubscribeLink(user))}


License
-------

The Apache Software License, Version 2.0

http://www.apache.org/licenses/LICENSE-2.0.txt