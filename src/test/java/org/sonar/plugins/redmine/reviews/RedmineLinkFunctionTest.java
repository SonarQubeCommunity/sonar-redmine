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
package org.sonar.plugins.redmine.reviews;

import com.taskadapter.redmineapi.bean.Issue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.config.Settings;
import org.sonar.api.i18n.I18n;
import org.sonar.api.issue.action.Function.Context;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.HashMap;
import java.util.Locale;
import org.junit.Ignore;
import org.mockito.Matchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import org.sonar.api.issue.action.Function;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineLinkFunctionTest {

  private RedmineLinkFunction linkFunction;
  private RedmineAdapter redmineAdapter;
  private RedmineIssueFactory issueFactory;
  private I18n i18n;
  private Function.Context context;
  private org.sonar.api.issue.Issue sonarIssue;
  private Settings settings;
  private Issue redmineIssue = new Issue();

  private static final String PROJECT_KEY = "PROJECT_KEY";
  private static final String URL = "http://host";
  private static final Integer ISSUE_ID = 1;

  @Before
  public void setUpMocks() throws Exception {
    settings = new Settings();
    settings.setProperty(RedmineSettings.PROJECT_KEY, PROJECT_KEY);
    settings.setProperty(RedmineSettings.URL, URL);

    sonarIssue = new DefaultIssue();

    redmineIssue.setId(ISSUE_ID);

    context = mock(Function.Context.class);
    redmineAdapter = mock(RedmineAdapter.class);
    issueFactory = mock(RedmineIssueFactory.class);
    i18n = mock(I18n.class);

    when(context.issue()).thenReturn(sonarIssue);
    when(context.projectSettings()).thenReturn(settings);

    when(issueFactory.createRedmineIssue(eq(context.issue()), any(RedmineSettings.class))).thenReturn(redmineIssue);
    when(redmineAdapter.createIssue(PROJECT_KEY, redmineIssue)).thenReturn(redmineIssue);
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_COMMENT, null)).thenReturn("Comment: ");
    
  }

  @Test
  public void shouldExecute() {
    linkFunction = new RedmineLinkFunction(issueFactory, redmineAdapter, i18n);
    linkFunction.execute(context);

    verify(context).addComment("Comment: http://host/issues/1");
    verify(context).setAttribute(RedmineConstants.ISSUE_ID, "1");
    
  }
}
