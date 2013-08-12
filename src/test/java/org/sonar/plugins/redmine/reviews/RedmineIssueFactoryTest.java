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
package org.sonar.plugins.redmine.reviews;

import com.taskadapter.redmineapi.bean.Issue;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.i18n.I18n;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.HashMap;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedmineIssueFactoryTest {
  private I18n i18n;
  private RedmineIssueFactory redmineIssueFactory;
  private Settings settings;
  private RedmineSettings rSettings;
//  private Review review;

  @Before
  public void setUpMocks() throws Exception {
    i18n = mock(I18n.class);
    settings = mock(Settings.class);
    rSettings = mock(RedmineSettings.class);
//    review = mock(Review.class);

    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_SUBJECT_TEMPLATE, "")).thenReturn("New Subject");
    when(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITHOUT_MESSAGE, "")).thenReturn("New Description");

//    redmineIssueFactory = new RedmineIssueFactory(i18n, settings);
  }

  @Test
  public void shouldReturnValidIssue() {
//    Issue issue = redmineIssueFactory.createRedmineIssue(review, rSettings, new HashMap<String, String>());

//    assertThat(issue).isNotNull();
//    assertThat(issue.getSubject()).as("New Subject");
//    assertThat(issue.getDescription()).as("New Description");
//    assertThat(issue.getPriorityId()).isEqualTo(rSettings.getPriorityID());
//    assertThat(issue.getTracker().getId()).isEqualTo(rSettings.getTrackerID());
  }
}
