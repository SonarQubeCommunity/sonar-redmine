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
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(RedmineSensor.class);
  private final Settings settings;
  private final RedmineAdapter redmineAdapter;

  public RedmineSensor(Settings settings, RedmineAdapter redmineAdapter) {
    this.settings = settings;
    this.redmineAdapter = redmineAdapter;
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
    try {
      redmineAdapter.connectToHost(getHost(), getApiAccessKey());
      Map<String, Integer> issuesByPriority =
              redmineAdapter.collectProjectIssuesByPriority(getProjectKey());
      double totalIssues = 0;
      PropertiesBuilder<String, Integer> distribution = new PropertiesBuilder<String, Integer>();
      for (Map.Entry<String, Integer> entry : issuesByPriority.entrySet()) {
        totalIssues += entry.getValue();
        distribution.add(entry.getKey(), entry.getValue());
      }
      saveMeasures(context, totalIssues, distribution.buildData());
    } catch (RedmineException ex) {
      LOG.error("RedMine issues sensor failed to get project issues.", ex);
    }
  }

  protected void saveMeasures(SensorContext context, double totalPrioritiesCount, String priorityDistribution) {
    context.saveMeasure(new Measure(RedmineMetrics.ISSUES, totalPrioritiesCount));
    context.saveMeasure(new Measure(RedmineMetrics.ISSUES_BY_PRIORITY, priorityDistribution));
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
