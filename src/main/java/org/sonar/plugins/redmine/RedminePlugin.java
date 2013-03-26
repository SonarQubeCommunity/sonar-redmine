/*
 * Sonar Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.redmine;

import java.util.List;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.redmine.batch.RedmineSensor;
import org.sonar.plugins.redmine.client.RedmineAdapter;
import org.sonar.plugins.redmine.config.RedmineSettings;
import org.sonar.plugins.redmine.exceptions.RedmineGeneralException;
import org.sonar.plugins.redmine.exceptions.RedmineNotAuthorizedException;
import org.sonar.plugins.redmine.exceptions.RedmineNotFoundException;
import org.sonar.plugins.redmine.reviews.RedmineIssueFactory;
import org.sonar.plugins.redmine.reviews.RedmineLinkFunction;
import org.sonar.plugins.redmine.reviews.RedmineWorkflowBuilder;
import org.sonar.plugins.redmine.ui.RedmineSettingsPage;
import org.sonar.plugins.redmine.ui.RedmineWidget;

import com.google.common.collect.ImmutableList;
import com.taskadapter.redmineapi.NotAuthorizedException;
import com.taskadapter.redmineapi.NotFoundException;
import com.taskadapter.redmineapi.RedmineAuthenticationException;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineTransportException;

@Properties({
		@Property(
				key = RedmineSettings.HOST,
				name = "Redmine Host URL",
				description = "Example : http://demo.redmine.org/",
				global = true,
				defaultValue = "",
				module = false),
		@Property(
				key = RedmineSettings.API_ACCESS_KEY,
				name = "API Access Key",
				description = "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.",
				type = org.sonar.api.PropertyType.PASSWORD,
				global = true,
				module = false) })
public class RedminePlugin extends SonarPlugin {

	public static RedmineException wrapException(Exception e) {
		// Work around to be able to catch exceptions in ruby on rails controller
		// It seems not to be possible to catch exceptions which aren't part of
		// this plugin
		if (e instanceof RedmineAuthenticationException) {
			return new org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException(e.getMessage());
		} else if (e instanceof RedmineTransportException) {
			return new org.sonar.plugins.redmine.exceptions.RedmineTransportException(e.getMessage());
		} else if (e instanceof NotFoundException) {
			return new RedmineNotFoundException(e.getMessage());
		} else if (e instanceof NotAuthorizedException) {
			return new RedmineNotAuthorizedException(e.getMessage());
		} else if (e instanceof RedmineException) {
			return new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getMessage());
		} else {
			if (e.getCause() != null) {
				return new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getCause().getLocalizedMessage());
			} else {
				return new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getLocalizedMessage());
			}
		}
	}

	public List getExtensions() {
		return ImmutableList.of(
				// Definitions
				RedmineMetrics.class,
				// Batch
				RedmineSensor.class, RedmineAdapter.class, RedmineSettings.class,
				// Server
				RedmineIssueFactory.class,
				// UI
				RedmineWidget.class, RedmineSettingsPage.class,
				// Reviews
				RedmineLinkFunction.class, RedmineWorkflowBuilder.class,
				// Exceptions
				RedmineGeneralException.class, org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException.class,
				org.sonar.plugins.redmine.exceptions.RedmineTransportException.class, RedmineNotFoundException.class, RedmineNotAuthorizedException.class);
	}
}