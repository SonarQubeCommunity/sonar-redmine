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
package org.sonar.plugins.redmine.ui;

import org.sonar.api.i18n.I18n;
import org.sonar.api.web.NavigationSection;
import org.sonar.api.web.Page;
import org.sonar.plugins.redmine.RedmineConstants;

import java.util.Locale;

@NavigationSection(value = {NavigationSection.RESOURCE_CONFIGURATION})
public class RedmineSettingsPage implements Page {

  private final I18n i18n;

  public RedmineSettingsPage(I18n i18n) {
    this.i18n = i18n;
  }

  @Override
  public String getId() {
    return "/redmine_configuration";
  }

  @Override
  public String getTitle() {
    return i18n.message(Locale.getDefault(), RedmineConstants.CONFIGURATION_PAGE, null);
  }
}
