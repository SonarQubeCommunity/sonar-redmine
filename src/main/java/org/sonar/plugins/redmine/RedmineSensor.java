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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CountDistributionBuilder;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.List;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(RedmineSensor.class);
  private final RedmineSettings redmineSettings;
  private final RedmineAdapter redmineAdapter;

  public RedmineSensor(RedmineSettings redmineSettings, RedmineAdapter redmineAdapter) {
    this.redmineSettings = redmineSettings;
    this.redmineAdapter = redmineAdapter;
  }

  public boolean shouldExecuteOnProject(Project project) {
    if (redmineSettings.missingMandatoryParameters()) {
      LOG.info("Redmine issues sensor will not run as some parameters are missing.");
    }
    return project.isRoot() && !redmineSettings.missingMandatoryParameters();
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    try {
      redmineAdapter.connectToHost(redmineSettings.getHost(), redmineSettings.getApiAccessKey());
      List<Issue> issues = redmineAdapter.collectProjectIssues(redmineSettings.getProjectKey(), project.getAnalysisDate());
      double totalIssues = issues.size();

      CountDistributionBuilder issuesByDeveloper = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_DEVELOPER);
      CountDistributionBuilder issuesByPriority = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_PRIORITY);
      CountDistributionBuilder issuesByStatus = new CountDistributionBuilder(RedmineMetrics.ISSUES_BY_STATUS);

      for(Issue issue: issues) {
        issuesByDeveloper.add(issue.getAssignee() != null ? issue.getAssignee().getFullName() : "unassigned");
        issuesByPriority.add(issue.getPriorityText());
        issuesByStatus.add(issue.getStatusName());
      }

      String url = buildUrl();
      saveMeasures(context, totalIssues, url, issuesByDeveloper, issuesByPriority, issuesByStatus);
    } catch (RedmineException ex) {
      LOG.error("Redmine issues sensor failed to get project issues.", ex);
    }
  }

  private String buildUrl() {
    StringBuilder urlBuilder = new StringBuilder(redmineSettings.getHost());
    urlBuilder.append("/projects/");
    urlBuilder.append(redmineSettings.getProjectKey());
    return urlBuilder.toString();
  }

  protected void saveMeasures(SensorContext context, double totalPrioritiesCount, String url,
                              CountDistributionBuilder issuesByDeveloper, CountDistributionBuilder issuesByPriority, CountDistributionBuilder issuesByStatus) {
    Measure redmineIssues = new Measure(RedmineMetrics.ISSUES, totalPrioritiesCount);
    redmineIssues.setUrl(url);
    context.saveMeasure(redmineIssues);
    context.saveMeasure(issuesByDeveloper.build().setValue(totalPrioritiesCount));
    context.saveMeasure(issuesByPriority.build().setValue(totalPrioritiesCount));
    context.saveMeasure(issuesByStatus.build().setValue(totalPrioritiesCount));
  }

  @Override
  public String toString() {
    return "Redmine issues sensor";
  }
}
