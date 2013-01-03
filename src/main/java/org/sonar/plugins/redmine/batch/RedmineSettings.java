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
package org.sonar.plugins.redmine.batch;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.plugins.redmine.RedmineConstants;

public class RedmineSettings implements BatchExtension, ServerExtension {

  private Settings settings;

  public RedmineSettings(Settings settings) {
    this.settings = settings;
  }

  public String getHost() {
    return settings.getString(RedmineConstants.HOST);
  }

  public String getApiAccessKey() {
    return settings.getString(RedmineConstants.API_ACCESS_KEY);
  }

  public String getProjectKey() {
    return settings.getString(RedmineConstants.PROJECT_KEY);
  }

  public void setHost(String host){
    settings.setProperty(RedmineConstants.HOST, host);
  }
  
  public void setApiAccessKey(String apiAccessKey){
    settings.setProperty(RedmineConstants.API_ACCESS_KEY, apiAccessKey);
  }
  
  public void setProjectKey(String projectKey){
    settings.setProperty(RedmineConstants.PROJECT_KEY, projectKey);
  }
  
  public boolean missingMandatoryParameters() {
    return StringUtils.isEmpty(getHost())
            || StringUtils.isEmpty(getProjectKey())
            || StringUtils.isEmpty(getApiAccessKey());
  }
}
