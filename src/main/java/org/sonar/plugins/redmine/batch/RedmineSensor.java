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

import com.google.common.collect.Maps;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.Project;
import org.sonar.plugins.redmine.RedmineMetrics;
import org.sonar.plugins.redmine.RedminePlugin;

public class RedmineSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(RedmineSensor.class);
  private final Settings settings;

  public RedmineSensor(Settings settings) {
    this.settings = settings;
  }

  private String getHost() {
    return settings.getString(RedminePlugin.HOST);
  }

  private String getApiAccessKey() {
    return settings.getString(RedminePlugin.API_ACCESS_KEY);
  }

  private String getProjectKey() {
    return settings.getString(RedminePlugin.PROJECT_KEY);
  }

  public boolean shouldExecuteOnProject(Project project) {
    if (missingMandatoryParameters()) {
      LOG.info("RedMine issues sensor will not run as some parameters are missing.");
    }
    return project.isRoot() && !missingMandatoryParameters();
  }

  public void analyse(Project project, SensorContext context) {
    RedmineManager mgr = new RedmineManager(getHost(), getApiAccessKey());
    try {
      Map<String, Integer> issuesByPriority = collectIssuesByPriority(mgr);
      double totalIssues = 0;
      PropertiesBuilder<String, Integer> distribution = new PropertiesBuilder<String, Integer>();
      for (Map.Entry<String, Integer> entry : issuesByPriority.entrySet()) {
        totalIssues += entry.getValue();
        distribution.add(entry.getKey(), entry.getValue());
      }
      saveMeasures(context, totalIssues, distribution.buildData());
    } catch (RedmineException ex) {
      LOG.error("RedMine issues sensor failed to get issues.", ex);
    }
  }
  
  protected void saveMeasures(SensorContext context,double totalPrioritiesCount, String priorityDistribution) {
    Measure issuesMeasure = new Measure(RedmineMetrics.ISSUES, totalPrioritiesCount);
    issuesMeasure.setData(priorityDistribution);
    context.saveMeasure(issuesMeasure);
  }
  
  private Map<String, Integer> collectIssuesByPriority(RedmineManager mgr) throws RedmineException {
    List<Issue> issues = mgr.getIssues(getProjectKey(), null);
    Map<String, Integer> issuesByPriority = Maps.newHashMap();

    for (Issue issue : issues) {
      String priority = issue.getPriorityText();
      if (!issuesByPriority.containsKey(priority)) {
        issuesByPriority.put(priority, 1);
      } else {
        issuesByPriority.put(priority, issuesByPriority.get(priority) + 1);
      }
    }
    return issuesByPriority;
  }

  protected boolean missingMandatoryParameters() {
    return StringUtils.isEmpty(getHost())
            || StringUtils.isEmpty(getProjectKey())
            || StringUtils.isEmpty(getApiAccessKey());
  }

  @Override
  public String toString() {
    return "Redmine issues sensor";
  }
}
