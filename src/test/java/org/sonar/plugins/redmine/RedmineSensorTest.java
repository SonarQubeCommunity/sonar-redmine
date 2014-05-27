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
package org.sonar.plugins.redmine;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CountDistributionBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineSensorTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RedmineSensor sensor;
  private RedmineSettings redmineSettings;
  private RedmineAdapter redmineAdapter;
  private String url;

  private Issue createIssue(String developer, String priority, String status)
  {
    Issue issue = new Issue();
    if (developer != null) {
      User user = new User();
      user.setFullName(developer);
      issue.setAssignee(user);
    }
    issue.setPriorityText(priority);
    issue.setStatusName(status);
    return issue;
  }

  @Before
  public void setUp() throws RedmineException {
    redmineSettings = new RedmineSettings(new Settings());
    redmineAdapter = mock(RedmineAdapter.class);

    List<Issue> issues = new ArrayList<Issue>();
    issues.add(createIssue("Tim Tom", "Normal", "Assigned"));
    issues.add(createIssue("John Doe", "Normal", "Resolved"));
    issues.add(createIssue("John Doe", "Urgent", "New"));

    doNothing().when(redmineAdapter).connectToHost("http://my.Redmine.server", "project_key");
    when(redmineAdapter.collectProjectIssues("project_key")).thenReturn(issues);
    redmineSettings.setHost("http://my.Redmine.server");
    redmineSettings.setApiAccessKey("api_access_key");
    redmineSettings.setProjectKey("project_key");
    redmineSettings.setTrackerID(1);
    redmineSettings.setPriorityID(1);
    sensor = new RedmineSensor(redmineSettings, redmineAdapter);
    url = "http://my.Redmine.server/projects/project_key";
  }

  @Test
  public void testToString() throws Exception {
    assertThat(sensor.toString(), is("Redmine issues sensor"));
  }

  @Test
  public void shouldExecuteOnRootProjectWithAllParams() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);
    assertThat(sensor.shouldExecuteOnProject(project), is(true));
  }

  @Test
  public void shouldNotExecuteOnNonRootProject() throws Exception {
    assertThat(sensor.shouldExecuteOnProject(mock(Project.class)), is(false));
  }

  @Test
  public void shouldNotExecuteOnRootProjectifOneParamMissing() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);
    redmineSettings.setHost(null);
    sensor = new RedmineSensor(redmineSettings, redmineAdapter);
    assertThat(sensor.shouldExecuteOnProject(project), is(false));
  }

  @Test
  public void testSaveMeasures() {
    SensorContext context = mock(SensorContext.class);

    CountDistributionBuilder issuesByDeveloper = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_DEVELOPER);
    CountDistributionBuilder issuesByPriority = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_PRIORITY);
    CountDistributionBuilder issuesByStatus = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_STATUS);

    String priorityDistribution = "Normal=2;Urgent=1";
    issuesByPriority.add("Normal");
    issuesByPriority.add("Normal");
    issuesByPriority.add("Urgent");

    String developerDistribution = "John Doe=2;Tim Tom=1";
    issuesByDeveloper.add("John Doe");
    issuesByDeveloper.add("Tim Tom");
    issuesByDeveloper.add("John Doe");

    String statusDistribution = "Assigned=1;New=1;Resolved=1";
    issuesByStatus.add("Assigned");
    issuesByStatus.add("New");
    issuesByStatus.add("Resolved");

    sensor.saveMeasures(context, 3, url, issuesByDeveloper, issuesByPriority, issuesByStatus);

    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 3.0)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_DEVELOPER, developerDistribution)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_STATUS, statusDistribution)));
    verifyNoMoreInteractions(context);
  }

  @Test
  public void testAnalyze() {
    SensorContext context = mock(SensorContext.class);
    Project project = mock(Project.class);
    RedmineSensor spy = spy(sensor);
    spy.analyse(project, context);

    String priorityDistribution = "Normal=2;Urgent=1";
    String developerDistribution = "John Doe=2;Tim Tom=1";
    String statusDistribution = "Assigned=1;New=1;Resolved=1";

    verify(spy).saveMeasures(eq(context), eq(3.0), eq(url),
        any(CountDistributionBuilder.class), any(CountDistributionBuilder.class), any(CountDistributionBuilder.class));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 3.0)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_DEVELOPER, developerDistribution)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_STATUS, statusDistribution)));
    verifyNoMoreInteractions(context);
  }

}
