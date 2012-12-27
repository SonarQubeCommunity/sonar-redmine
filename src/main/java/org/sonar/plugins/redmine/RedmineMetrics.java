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
package org.sonar.plugins.redmine;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public final class RedmineMetrics implements Metrics {
  public static final String ISSUES_DOMAIN = "Issues";
  public static final String ISSUES_KEY = "redmine-issues";

  public static final Metric ISSUES = new Metric.Builder(ISSUES_KEY, "RedMine Issues", Metric.ValueType.INT)
      .setDescription("Number of RedMine Issues")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(ISSUES_DOMAIN)
      .create();

  public List<Metric> getMetrics() {
    return ImmutableList.of(ISSUES);
  }
}
