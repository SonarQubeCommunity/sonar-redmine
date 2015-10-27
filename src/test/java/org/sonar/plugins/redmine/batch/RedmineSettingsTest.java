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
package org.sonar.plugins.redmine.batch;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.redmine.config.RedmineSettings;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RedmineSettingsTest {

  RedmineSettings redmineSettings;
  Settings settings = new Settings();

  @Before
  public void setUp() {
    redmineSettings = new RedmineSettings(settings);
    redmineSettings.setHost("host");
    redmineSettings.setApiAccessKey("apiAccessKey");
    redmineSettings.setProjectKey("projectKey");
  }

  @Test
  public void missingMandatoryParametersShouldReturnFalse() throws Exception {
    assertThat(redmineSettings.missingMandatoryParameters(), is(false));
  }

  @Test
  public void missingMandatoryParametersShouldReturnTrueIfApiAccessKeyIsMissing() throws Exception {
    settings.removeProperty(RedmineSettings.API_ACCESS_KEY);
    redmineSettings = new RedmineSettings(settings);
    assertThat(redmineSettings.missingMandatoryParameters(), is(true));
  }

  @Test
  public void missingMandatoryParametersShouldReturnTrueIfHostIsMissing() throws Exception {
    settings.removeProperty(RedmineSettings.URL);
    redmineSettings = new RedmineSettings(settings);
    assertThat(redmineSettings.missingMandatoryParameters(), is(true));
  }

  @Test
  public void missingMandatoryParametersShouldReturnTrueIfProjectKeyIsMissing() throws Exception {
    settings.removeProperty(RedmineSettings.PROJECT_KEY);
    redmineSettings = new RedmineSettings(settings);
    assertThat(redmineSettings.missingMandatoryParameters(), is(true));
  }

}
