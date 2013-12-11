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
package org.sonar.plugins.redmine.reviews;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.i18n.I18n;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.sonar.api.CoreProperties;
import org.sonar.api.issue.Issue;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import static org.fest.assertions.api.Assertions.*;

public class RedmineIssueFactoryTest {
  private I18n i18n;
  private RedmineIssueFactory redmineIssueFactory;
  private Settings settings;
  private RedmineSettings redmineSettings;
  private RuleFinder ruleFinder;
  private Rule rule = new Rule();
  private final Issue issue = mock(Issue.class);
  private static final String ISSUE_KEY = "ISSUE_KEY";
  private static final String RULE_KEY = "RULE_KEY";
  private static final String RULE_NAME = "RULE_NAME";
  private static final String SERVER_BASE_URL = "http://baseurl";
  private static final String SUBJECT_NO_RULE = "Subject_No_Rule";
  private static final String DESCRIPTION_NO_RULE = "Description_No_Rule";
  private static final String SUBJECT_WITH_RULE = "Subject_With_Rule";
  private static final String DESCRIPTION_WITH_RULE = "Description_With_Rule";
  private static final String ISSUE_URL = "http://baseurl/issue/show/ISSUE_KEY";
  private static final Integer PRIORITY_ID = 10;
  private static final Integer TRACKER_ID = 1;
  
  @Before
  public void setUpMocks() {
    i18n = mock(I18n.class);
    settings = mock(Settings.class);
    redmineSettings = mock(RedmineSettings.class);
    ruleFinder = mock(RuleFinder.class);
    
    when(issue.key()).thenReturn(ISSUE_KEY);
    
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_SUBJECT_TEMPLATE_NO_RULE, ISSUE_KEY)).thenReturn(SUBJECT_NO_RULE);
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITHOUT_MESSAGE, ISSUE_URL)).thenReturn(DESCRIPTION_NO_RULE);

    when(ruleFinder.findByKey(issue.ruleKey())).thenReturn(rule);
    
    when(redmineSettings.getTrackerID()).thenReturn(TRACKER_ID);
    when(redmineSettings.getPriorityID()).thenReturn(PRIORITY_ID);
    
    when(settings.getString(CoreProperties.SERVER_BASE_URL)).thenReturn(SERVER_BASE_URL);
    
    redmineIssueFactory = new RedmineIssueFactory(i18n, settings, ruleFinder);
  }

  @Test
  public void shouldCreateRedmineIssueWithNoRule() {
    when(ruleFinder.findByKey(issue.ruleKey())).thenReturn(null);
    com.taskadapter.redmineapi.bean.Issue redmineIssue = redmineIssueFactory.createRedmineIssue(issue, redmineSettings);
    
    assertThat(redmineIssue.getSubject()).isEqualTo(SUBJECT_NO_RULE);
    assertThat(redmineIssue.getDescription()).isEqualTo(DESCRIPTION_NO_RULE);
    assertThat(redmineIssue.getPriorityId()).isEqualTo(PRIORITY_ID);
    assertThat(redmineIssue.getTracker().getId()).isEqualTo(TRACKER_ID);
  }

  @Test
  public void shouldCreateRedmineIssueWithRule() {
    rule.setKey(RULE_KEY);
    rule.setName(RULE_NAME);
    
    when(ruleFinder.findByKey(issue.ruleKey())).thenReturn(rule);
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_SUBJECT_TEMPLATE, ISSUE_KEY, RULE_NAME)).thenReturn(SUBJECT_WITH_RULE);
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITH_MESSAGE, RULE_KEY + " - " + RULE_NAME, ISSUE_URL)).thenReturn(DESCRIPTION_WITH_RULE);

    com.taskadapter.redmineapi.bean.Issue redmineIssue = redmineIssueFactory.createRedmineIssue(issue, redmineSettings);

    assertThat(redmineIssue.getSubject()).isEqualTo(SUBJECT_WITH_RULE);
    assertThat(redmineIssue.getDescription()).isEqualTo(DESCRIPTION_WITH_RULE);
    assertThat(redmineIssue.getPriorityId()).isEqualTo(PRIORITY_ID);
    assertThat(redmineIssue.getTracker().getId()).isEqualTo(TRACKER_ID);
  }

}
