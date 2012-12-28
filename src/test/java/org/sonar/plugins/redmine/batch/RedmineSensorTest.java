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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.redmine.RedmineMetrics;
import org.sonar.plugins.redmine.RedminePlugin;
import org.sonar.plugins.redmine.client.RedmineAdapter;


public class RedmineSensorTest {
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RedmineSensor sensor;
  private Settings settings;
  private RedmineAdapter redmineAdapter;

  @Before
  public void setUp() {
    settings = new Settings();
    redmineAdapter = new RedmineAdapter();
    settings.setProperty(RedminePlugin.HOST, "http://my.Redmine.server");
    settings.setProperty(RedminePlugin.API_ACCESS_KEY, "api_access_key");
    settings.setProperty(RedminePlugin.PROJECT_KEY, "project_key");
    sensor = new RedmineSensor(settings,redmineAdapter);
  }

  @Test
  public void testToString() throws Exception {
    assertThat(sensor.toString(),is("Redmine issues sensor"));
  }

  @Test
  public void testPresenceOfProperties() throws Exception {
    assertThat(sensor.missingMandatoryParameters(),is(false));

    settings.removeProperty(RedminePlugin.API_ACCESS_KEY);
    sensor = new RedmineSensor(settings,redmineAdapter);
    assertThat(sensor.missingMandatoryParameters(),is(true));

    settings.removeProperty(RedminePlugin.HOST);
    sensor = new RedmineSensor(settings,redmineAdapter);
    assertThat(sensor.missingMandatoryParameters(),is(true));

    settings.removeProperty(RedminePlugin.PROJECT_KEY);
    sensor = new RedmineSensor(settings,redmineAdapter);
    assertThat(sensor.missingMandatoryParameters(),is(true));

  }

  @Test
  public void shouldExecuteOnRootProjectWithAllParams() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);
    assertThat(sensor.shouldExecuteOnProject(project),is(true));
  }

  @Test
  public void shouldNotExecuteOnNonRootProject() throws Exception {
    assertThat(sensor.shouldExecuteOnProject(mock(Project.class)),is(false));
  }

  @Test
  public void shouldNotExecuteOnRootProjectifOneParamMissing() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);
    settings.removeProperty(RedminePlugin.HOST);
    sensor = new RedmineSensor(settings, redmineAdapter);
    assertThat(sensor.shouldExecuteOnProject(project),is(false));
  }

  @Test
  public void testSaveMeasures() {
    SensorContext context = mock(SensorContext.class);
    String priorityDistribution = "Normal=2;Urgent=1";

    sensor.saveMeasures(context, 3, priorityDistribution);

    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 3.0)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution)));
    verifyNoMoreInteractions(context);
  }

 
  /*@Test
  public void shouldCollectIssuesByPriority() throws Exception {
    RemoteFilter filter = new RemoteFilter();
    filter.setId("1");
    RedmineSoapService RedmineSoapService = mock(RedmineSoapService.class);
    RemoteIssue issue1 = new RemoteIssue();
    issue1.setPriority("minor");
    RemoteIssue issue2 = new RemoteIssue();
    issue2.setPriority("critical");
    RemoteIssue issue3 = new RemoteIssue();
    issue3.setPriority("critical");
    when(RedmineSoapService.getIssuesFromFilter("token", "1")).thenReturn(new RemoteIssue[] {issue1, issue2, issue3});

    Map<String, Integer> foundIssues = sensor.collectIssuesByPriority(RedmineSoapService, "token", filter);
    assertThat(foundIssues.size()).isEqualTo(2);
    assertThat(foundIssues.get("critical")).isEqualTo(2);
    assertThat(foundIssues.get("minor")).isEqualTo(1);
  }

 */
}
