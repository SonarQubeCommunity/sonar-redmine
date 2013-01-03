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

import com.taskadapter.redmineapi.RedmineException;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.redmine.RedmineMetrics;
import org.sonar.plugins.redmine.client.RedmineAdapter;


public class RedmineSensorTest {
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RedmineSensor sensor;
  private RedmineSettings redmineSettings;
  private RedmineAdapter redmineAdapter;
  private String url;

  @Before
  public void setUp() throws RedmineException {
    redmineSettings = new RedmineSettings(new Settings());
    redmineAdapter = mock(RedmineAdapter.class);
    Map<String, Integer> issuesByPriority = new HashMap<String, Integer>();
    issuesByPriority.put("Normal", 2);
    issuesByPriority.put("Urgent", 1);
    doNothing().when(redmineAdapter).connectToHost("http://my.Redmine.server", "project_key");
    when(redmineAdapter.collectProjectIssuesByPriority("project_key")).thenReturn(issuesByPriority);
    redmineSettings.setHost("http://my.Redmine.server");
    redmineSettings.setApiAccessKey( "api_access_key");
    redmineSettings.setProjectKey("project_key");
    sensor = new RedmineSensor(redmineSettings,redmineAdapter);
    url = "http://my.Redmine.server/projects/project_key";
  }

  @Test
  public void testToString() throws Exception {
    assertThat(sensor.toString(),is("Redmine issues sensor"));
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
    redmineSettings.setHost(null);
    sensor = new RedmineSensor(redmineSettings, redmineAdapter);
    assertThat(sensor.shouldExecuteOnProject(project),is(false));
  }

  @Test
  public void testSaveMeasures() {
    SensorContext context = mock(SensorContext.class);
    String priorityDistribution = "Normal=2;Urgent=1";

    sensor.saveMeasures(context, 3, url, priorityDistribution);

    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 3.0)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution)));
    verifyNoMoreInteractions(context);
  }

  @Test
  public void testAnalyze() {
    SensorContext context = mock(SensorContext.class);
    Project project = mock(Project.class);
    RedmineSensor spy = spy(sensor);
    spy.analyse(project, context);
    String priorityDistribution = "Normal=2;Urgent=1";

    verify(spy).saveMeasures(context, 3.0, url, priorityDistribution);
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES, 3.0)));
    verify(context).saveMeasure(argThat(new IsMeasure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution)));
    verifyNoMoreInteractions(context);
  }

}
