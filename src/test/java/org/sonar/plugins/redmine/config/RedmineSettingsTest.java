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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import static org.mockito.Mockito.*;
import static org.fest.assertions.api.Assertions.*;

public class RedmineSettingsTest {
  
  private final Settings settings = new Settings();
  private final Settings spiedSettings = spy(settings);
  private RedmineSettings redmineSettings;
  private static final String URL = "URL";
  private static final String API_ACCESS_KEY = "API_ACCESS_KEY";
  private static final String PROJECT_KEY = "PROJECT_KEY";
  private static final Integer PRIORITY_ID = 5;
  private static final Integer TRACKER_ID = 1;
  
  @Before
  public void setUp() {
    settings.setProperty(RedmineSettings.URL,URL);
    settings.setProperty(RedmineSettings.API_ACCESS_KEY,API_ACCESS_KEY);
    settings.setProperty(RedmineSettings.PROJECT_KEY,PROJECT_KEY);
    settings.setProperty(RedmineSettings.PRIORITY_ID,PRIORITY_ID);
    settings.setProperty(RedmineSettings.TRACKER_ID,TRACKER_ID);
    
    redmineSettings = new RedmineSettings(spiedSettings);
  }
 
  @Test
  public void shouldGetHost() {
    assertThat(redmineSettings.getHost()).isEqualTo(URL);
  }

  @Test
  public void shouldGetApiAccessKey() {
    assertThat(redmineSettings.getApiAccessKey()).isEqualTo(API_ACCESS_KEY);  
  }

  @Test
  public void shouldtGetProjectKey() {
    assertThat(redmineSettings.getProjectKey()).isEqualTo(PROJECT_KEY);  
  }

  @Test
  public void shouldGetPriorityID() {
    assertThat(redmineSettings.getPriorityID()).isEqualTo(PRIORITY_ID);  
  }

  @Test
  public void shouldGetTrackerID() {
    assertThat(redmineSettings.getTrackerID()).isEqualTo(TRACKER_ID);  
  }

  @Test
  public void shouldSetHost() {
    redmineSettings.setHost(URL);
    verify(spiedSettings).setProperty(RedmineSettings.URL,URL);
    assertThat(redmineSettings.getHost()).isEqualTo(URL);
  }

  @Test
  public void shouldSetApiAccessKey() {
    redmineSettings.setApiAccessKey(API_ACCESS_KEY);
    verify(spiedSettings).setProperty(RedmineSettings.API_ACCESS_KEY,API_ACCESS_KEY);
    assertThat(redmineSettings.getApiAccessKey()).isEqualTo(API_ACCESS_KEY);  
  }

  @Test
  public void shouldtSetProjectKey() {
    redmineSettings.setProjectKey(PROJECT_KEY);
    verify(spiedSettings).setProperty(RedmineSettings.PROJECT_KEY,PROJECT_KEY);
    assertThat(redmineSettings.getProjectKey()).isEqualTo(PROJECT_KEY);  
  }

  @Test
  public void shouldSetPriorityID() {
    redmineSettings.setPriorityID(PRIORITY_ID);
    verify(spiedSettings).setProperty(RedmineSettings.PRIORITY_ID,PRIORITY_ID);
    assertThat(redmineSettings.getPriorityID()).isEqualTo(PRIORITY_ID);  
  }

  @Test
  public void shouldSetTrackerID() {
    redmineSettings.setTrackerID(TRACKER_ID);
    verify(spiedSettings).setProperty(RedmineSettings.TRACKER_ID,TRACKER_ID);
    assertThat(redmineSettings.getTrackerID()).isEqualTo(TRACKER_ID);  
  }

  @Test
  public void shouldCheckMissingHost() {
    redmineSettings.setHost("");
    assertThat(redmineSettings.missingMandatoryParameters()).isTrue();

  }
  @Test
  public void shouldCheckMissingApiAccessKey() {
    redmineSettings.setApiAccessKey("");
    assertThat(redmineSettings.missingMandatoryParameters()).isTrue();

  }
  @Test
  public void shouldCheckMissingProjectKey() {
    redmineSettings.setProjectKey("");
    assertThat(redmineSettings.missingMandatoryParameters()).isTrue();

  }
  @Test
  public void allParametersAreSet() {
    assertThat(redmineSettings.missingMandatoryParameters()).isFalse();
  }
  
}
