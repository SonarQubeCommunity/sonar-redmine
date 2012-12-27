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

import java.util.Map;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.redmine.RedminePlugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.sonar.plugins.redmine.RedmineMetrics;

public class RedmineSensorTest {
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RedmineSensor sensor;
  private Settings settings;

  @Before
  public void setUp() {
    settings = new Settings();
    settings.setProperty(RedminePlugin.HOST, "http://my.Redmine.server");
    settings.setProperty(RedminePlugin.API_ACCESS_KEY, "api_access_key");
    settings.setProperty(RedminePlugin.PROJECT_KEY, "project_key");
    sensor = new RedmineSensor(settings);
  }

  @Test
  public void testToString() throws Exception {
    assertThat(sensor.toString(),is("Redmine issues sensor"));
  }

  /*@Test
  public void testPresenceOfProperties() throws Exception {
    assertThat(sensor.missingMandatoryParameters()).isEqualTo(false);

    settings.removeProperty(RedmineConstants.PASSWORD_PROPERTY);
    sensor = new RedmineSensor(settings);
    assertThat(sensor.missingMandatoryParameters()).isEqualTo(true);

    settings.removeProperty(RedmineConstants.USERNAME_PROPERTY);
    sensor = new RedmineSensor(settings);
    assertThat(sensor.missingMandatoryParameters()).isEqualTo(true);

    settings.removeProperty(RedmineConstants.FILTER_PROPERTY);
    sensor = new RedmineSensor(settings);
    assertThat(sensor.missingMandatoryParameters()).isEqualTo(true);

    settings.removeProperty(RedmineConstants.SERVER_URL_PROPERTY);
    sensor = new RedmineSensor(settings);
    assertThat(sensor.missingMandatoryParameters()).isEqualTo(true);
  }

  @Test
  public void shouldExecuteOnRootProjectWithAllParams() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);

    assertThat(sensor.shouldExecuteOnProject(project)).isEqualTo(true);
  }

  @Test
  public void shouldNotExecuteOnNonRootProject() throws Exception {
    assertThat(sensor.shouldExecuteOnProject(mock(Project.class))).isEqualTo(false);
  }

  @Test
  public void shouldNotExecuteOnRootProjectifOneParamMissing() throws Exception {
    Project project = mock(Project.class);
    when(project.isRoot()).thenReturn(true).thenReturn(false);

    settings.removeProperty(RedmineConstants.SERVER_URL_PROPERTY);
    sensor = new RedmineSensor(settings);

    assertThat(sensor.shouldExecuteOnProject(project)).isEqualTo(false);
  }

  @Test
  public void testSaveMeasures() {
    SensorContext context = mock(SensorContext.class);
    String url = "http://localhost/Redmine";
    String priorityDistribution = "Critical=1";

    sensor.saveMeasures(context, url, 1, priorityDistribution);

    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 1.0, priorityDistribution)));
    verifyNoMoreInteractions(context);
  }

 
  @Test
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
