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

import com.google.common.collect.ImmutableList;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.List;

public class RedmineMetrics implements Metrics {

  public static final Metric ISSUES = new Metric.Builder(RedmineConstants.ISSUES_KEY, "Redmine Issues", Metric.ValueType.INT)
      .setDescription("Number of Redmine Issues")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(true)
      .setDomain(RedmineConstants.ISSUES_DOMAIN)
      .create();

  public static final Metric ISSUES_BY_PRIORITY = new Metric.Builder(RedmineConstants.ISSUES_BY_PRIORITY_KEY, "Redmine Issues by priority",
      Metric.ValueType.DISTRIB)
      .setDescription("Number of Redmine issues by priority")
      .setQualitative(true)
      .setDomain(RedmineConstants.ISSUES_DOMAIN)
      .create();

  public static final Metric ISSUES_BY_DEVELOPER = new Metric.Builder(RedmineConstants.ISSUES_BY_DEVELOPER_KEY, "Redmine Issues by developer",
      Metric.ValueType.DISTRIB)
      .setDescription("Number of Redmine issues by developer")
      .setQualitative(true)
      .setDomain(RedmineConstants.ISSUES_DOMAIN)
      .create();

  public static final Metric ISSUES_BY_STATUS = new Metric.Builder(RedmineConstants.ISSUES_BY_STATUS_KEY, "Redmine Issues by status",
      Metric.ValueType.DISTRIB)
      .setDescription("Number of Redmine issues by status")
      .setQualitative(true)
      .setDomain(RedmineConstants.ISSUES_DOMAIN)
      .create();


  public List<Metric> getMetrics() {
    return ImmutableList.of(ISSUES, ISSUES_BY_DEVELOPER, ISSUES_BY_PRIORITY, ISSUES_BY_STATUS);
  }
}
