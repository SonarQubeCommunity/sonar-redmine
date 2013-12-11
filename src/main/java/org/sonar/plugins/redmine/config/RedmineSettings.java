/*
 * SonarQube Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU and Christian Schulz
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
package org.sonar.plugins.redmine.config;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

public class RedmineSettings implements BatchExtension, ServerExtension {
  public static final String URL = "sonar.redmine.url";

  public static final String API_ACCESS_KEY = "sonar.redmine.api-access-key";
  public static final String PROJECT_KEY = "sonar.redmine.project-key";

  public static final String PRIORITY_ID = "sonar.redmine.priority-id";
  public static final String TRACKER_ID = "sonar.redmine.tracker-id";

  private Settings settings;

  public RedmineSettings(Settings settings) {
    this.settings = settings;
  }

  public String getHost() {
    return settings.getString(URL);
  }

  public String getApiAccessKey() {
    return settings.getString(API_ACCESS_KEY);
  }

  public String getProjectKey() {
    return settings.getString(PROJECT_KEY);
  }

  public int getPriorityID() {
    return settings.getInt(PRIORITY_ID);
  }

  public int getTrackerID() {
    return settings.getInt(TRACKER_ID);
  }

  public void setHost(String host) {
    settings.setProperty(URL, host);
  }

  public void setApiAccessKey(String apiAccessKey) {
    settings.setProperty(API_ACCESS_KEY, apiAccessKey);
  }

  public void setProjectKey(String projectKey) {
    settings.setProperty(PROJECT_KEY, projectKey);
  }

  public void setPriorityID(int priorityID) {
    settings.setProperty(PRIORITY_ID, priorityID);
  }

  public void setTrackerID(int trackerID) {
    settings.setProperty(TRACKER_ID, trackerID);
  }

  public boolean missingMandatoryParameters() {
    return StringUtils.isEmpty(getHost()) || StringUtils.isEmpty(getProjectKey()) || StringUtils.isEmpty(getApiAccessKey());
  }
}
