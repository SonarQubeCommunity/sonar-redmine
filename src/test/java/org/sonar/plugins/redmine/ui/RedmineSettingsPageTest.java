/*
 * SonarQube Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU and Christian Schulz
 * sonarqube@googlegroups.com
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
package org.sonar.plugins.redmine.ui;

import java.util.Locale;
import org.junit.Test;
import org.junit.Before;
import org.sonar.api.i18n.I18n;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.fest.assertions.api.Assertions.*;
import org.sonar.plugins.redmine.RedmineConstants;

public class RedmineSettingsPageTest {

  private RedmineSettingsPage settingsPage;
  private I18n i18n;
  
  @Before
  public void init(){
    i18n = mock(I18n.class);
    settingsPage = new RedmineSettingsPage(i18n);
  }
  
  @Test
  public void testGetId() {
    assertThat(settingsPage.getId()).isEqualTo("/redmine_configuration");
  }

  @Test
  public void testGetTitle() {
    
    String title = "title";
    when(i18n.message(Locale.getDefault(), RedmineConstants.CONFIGURATION_PAGE, null)).thenReturn(title);
    assertThat(settingsPage.getTitle()).isEqualTo(title);
  }
  
}
