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
package org.sonar.plugins.redmine.client;

import com.google.common.collect.ImmutableList;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssuePriority;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.User;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import static org.fest.assertions.api.Assertions.*;
import org.fest.assertions.data.MapEntry;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RedmineAdapter.class)

public class RedmineAdapterTest {

  private static final String HOST = "host";
  private static final String APIKEY = "apiKey";
  private final RedmineAdapter redmineAdapter = new RedmineAdapter();
  private RedmineManager redmineManager;
  private final User currentUser = new User();
  private final Project project = new Project();
  private static final Integer USER_ID = 1;
  private static final Integer PROJECT_ID = 100;
  private static final String PROJECT_KEY = "ProjectKey";

  @Before
  public void setUp() throws Exception {
    redmineManager = mock(RedmineManager.class);
    PowerMockito.whenNew(RedmineManager.class).withArguments(HOST, APIKEY).thenReturn(redmineManager);

    currentUser.setId(USER_ID);
    project.setId(PROJECT_ID);
    when(redmineManager.getCurrentUser()).thenReturn(currentUser);
    when(redmineManager.getUserById(USER_ID)).thenReturn(currentUser);
    when(redmineManager.getProjectByKey(PROJECT_KEY)).thenReturn(project);
  }

  @Test
  public void should_connect_to_host() {
    try {
      redmineAdapter.connectToHost(HOST, APIKEY);
    } catch (Exception ex) {
      fail(ex.getMessage(), ex);
    }
  }

  @Test
  public void connect_to_host_should_throw_exception() throws Exception {
    try {
      PowerMockito.whenNew(RedmineManager.class).withArguments(HOST, APIKEY).thenThrow(Exception.class);
      redmineAdapter.connectToHost(HOST, APIKEY);
      failBecauseExceptionWasNotThrown(RedmineException.class);
    } catch (RedmineException e) {
      assertThat(e).isInstanceOf(RedmineException.class);
      assertThat(e.getCause()).isInstanceOf(Exception.class);
    }
  }

  @Test
  public void should_get_current_user() throws RedmineException {
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.getCurrentUser()).isEqualTo(currentUser);
  }

  @Test
  public void get_current_user_should_throw_exception() {
    try {
      redmineAdapter.connectToHost(HOST, APIKEY);
      when(redmineManager.getCurrentUser()).thenThrow(RedmineException.class);
      redmineAdapter.getCurrentUser();
      failBecauseExceptionWasNotThrown(RedmineException.class);
    } catch (RedmineException ex) {
      assertWrappedException(ex);
    }
  }

  @Test
  public void should_get_current_user_with_id() throws RedmineException {
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.getUser(USER_ID)).isEqualTo(currentUser);
  }

  @Test
  public void get_current_user_with_id_should_throw_exception() {
    try {
      redmineAdapter.connectToHost(HOST, APIKEY);
      when(redmineManager.getUserById(USER_ID)).thenThrow(RedmineException.class);
      redmineAdapter.getUser(USER_ID);
      failBecauseExceptionWasNotThrown(RedmineException.class);
    } catch (RedmineException ex) {
      assertWrappedException(ex);
    }
  }

  @Test
  public void should_get_project() throws RedmineException {
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.getProject(PROJECT_KEY)).isEqualTo(project);
  }

  @Test
  public void get_project_should_throw_exception() {
    try {
      redmineAdapter.connectToHost(HOST, APIKEY);
      when(redmineManager.getProjectByKey(PROJECT_KEY)).thenThrow(RedmineException.class);
      redmineAdapter.getProject(PROJECT_KEY);
      failBecauseExceptionWasNotThrown(RedmineException.class);
    } catch (RedmineException ex) {
      assertWrappedException(ex);
    }
  }

  @Test
  public void user_should_be_member_of_project() throws RedmineException {

    Membership membership = new Membership();
    membership.setProject(project);
    List<Membership> memberships = ImmutableList.of(membership);
    currentUser.setMemberships(memberships);
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.isMemberOfProject(currentUser, project)).isTrue();
  }

  @Test
  public void user_should_not_be_member_of_project() throws RedmineException {

    Membership membership = new Membership();
    Project otherProject = new Project();
    otherProject.setId(999);
    membership.setProject(otherProject);
    List<Membership> memberships = ImmutableList.of(membership);
    currentUser.setMemberships(memberships);
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.isMemberOfProject(currentUser, project)).isFalse();
  }

  @Test
  public void should_get_issue_priorities() throws RedmineException {
    List<IssuePriority> issuePriorites = mock(List.class);
    when(redmineManager.getIssuePriorities()).thenReturn(issuePriorites);
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.getIssuePriorities()).isEqualTo(issuePriorites);
  }

  @Test
  public void get_issue_priorities_should_throw_exception() {
    try {
      when(redmineManager.getIssuePriorities()).thenThrow(RedmineException.class);
      redmineAdapter.connectToHost(HOST, APIKEY);
      redmineAdapter.getIssuePriorities();
      failBecauseExceptionWasNotThrown(RedmineException.class);
    } catch (RedmineException ex) {
      assertWrappedException(ex);
    }
  }

  @Test
  public void should_create_issue() throws RedmineException {
    Issue issue = mock(Issue.class);
    Issue createdIssue = mock(Issue.class);
    when(redmineManager.createIssue(PROJECT_KEY, issue)).thenReturn(createdIssue);
    redmineAdapter.connectToHost(HOST, APIKEY);
    assertThat(redmineAdapter.createIssue(PROJECT_KEY, issue)).isEqualTo(createdIssue);
  }
  
  @Test
  public void should_collect_project_issues_by_priority() throws RedmineException {
    Issue issue1 = mock(Issue.class);
    Issue issue2 = mock(Issue.class);
    Issue issue3 = mock(Issue.class);
    
    when(issue1.getPriorityText()).thenReturn("P1");
    when(issue2.getPriorityText()).thenReturn("P2");
    when(issue3.getPriorityText()).thenReturn("P1");
    
    List<Issue> issues = ImmutableList.of(issue1, issue2, issue3);
    
    when(redmineManager.getIssues(PROJECT_KEY, null)).thenReturn(issues);
    redmineAdapter.connectToHost(HOST, APIKEY);
    Map<String,Integer> issuesPerPriority = redmineAdapter.collectProjectIssuesByPriority(PROJECT_KEY);
    
    assertThat(issuesPerPriority).hasSize(2).contains(MapEntry.entry("P1", 2)).contains(MapEntry.entry("P2", 1));
  }
  

  private void assertWrappedException(RedmineException ex) {
    assertThat(ex).isInstanceOf(RedmineException.class);
    assertThat(ex.getCause()).isInstanceOf(RedmineException.class);
  }

}
