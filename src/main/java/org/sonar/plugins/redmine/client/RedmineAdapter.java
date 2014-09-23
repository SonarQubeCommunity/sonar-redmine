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

import com.google.common.collect.Maps;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssuePriority;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.User;
import java.util.HashMap;
import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections.ListUtils;
import org.sonar.plugins.redmine.exceptions.ExceptionUtil;

public class RedmineAdapter implements BatchExtension, ServerExtension {

  protected RedmineManager redmineMgr;

  public void connectToHost(final String host, final String apiKey) throws RedmineException {
    try {
      redmineMgr = new RedmineManager(host, apiKey);
    } catch (Exception e) {
      throw ExceptionUtil.wrapException(e);
    }
  }

  public User getCurrentUser() throws RedmineException {
    try {
      // It is required to get the user with its id to fetch project memberships
      return getUser(redmineMgr.getCurrentUser().getId());
    } catch (RedmineException e) {
      throw ExceptionUtil.wrapException(e);
    }
  }

  public Project getProject(String projectKey) throws RedmineException {
    try {
      return redmineMgr.getProjectByKey(projectKey);
    } catch (RedmineException e) {
      throw ExceptionUtil.wrapException(e);
    }
  }

  public User getUser(int userId) throws RedmineException {
    try {
      return redmineMgr.getUserById(userId);
    } catch (RedmineException e) {
      throw ExceptionUtil.wrapException(e);
    }
  }

  public boolean isMemberOfProject(User user, Project project) {
    boolean isMemberOfProject = false;
    List<Membership> memberships = user.getMemberships();
    int projectId = project.getId();

    for (Membership membership : memberships) {
      if (membership.getProject().getId() == projectId) {
        isMemberOfProject = true;
      }
    }

    return isMemberOfProject;
  }

  public List<IssuePriority> getIssuePriorities() throws RedmineException {
    try {
      return redmineMgr.getIssuePriorities();
    } catch (RedmineException e) {
      throw ExceptionUtil.wrapException(e);
    }
  }

  public Issue createIssue(final String projectKey, final Issue issue) throws RedmineException {
    return redmineMgr.createIssue(projectKey, issue);
  }

  public List<Issue> collectProjectIssues(final String projectKey, final java.util.Date projectDate) throws RedmineException {
    final String date = projectDate == null
        ? null
        : new java.text.SimpleDateFormat("yyyy-MM-dd").format(projectDate);

    //Get open issues first
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("project_id", projectKey);
    parameters.put("status_id", "open");
    if (projectDate != null) {
      parameters.put("created_on", "<=" + date);
    }
    List<Issue> issues = redmineMgr.getIssues(parameters);

    //If a date was specified, also get the issues that have been closed since
    if (projectDate != null) {
      parameters.put("status_id", "closed");
      parameters.put("created_on", "<=" + date);
      parameters.put("closed_on", ">=" + date);
      issues = ListUtils.union(issues, redmineMgr.getIssues(parameters));
    }
    return issues;
  }

  public Map<String, Integer> collectProjectIssuesByPriority(final String projectKey, final java.util.Date projectDate) throws RedmineException {

    List<Issue> issues = collectProjectIssues(projectKey, projectDate);
    Map<String, Integer> issuesByPriority = Maps.newHashMap();

    for (Issue issue : issues) {
      String priority = issue.getPriorityText();
      if (issuesByPriority.containsKey(priority)) {
        issuesByPriority.put(priority, issuesByPriority.get(priority) + 1);
      } else {
        issuesByPriority.put(priority, 1);
      }
    }
    return issuesByPriority;
  }
}
