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
package org.sonar.plugins.redmine.reviews;

import org.sonar.api.ServerExtension;
import org.sonar.api.issue.action.Actions;
import org.sonar.api.issue.condition.HasIssuePropertyCondition;
import org.sonar.api.issue.condition.IsUnResolved;
import org.sonar.api.issue.condition.NotCondition;
import org.sonar.plugins.redmine.RedmineConstants;

public class RedmineWorkflowBuilder implements ServerExtension {

  private final RedmineLinkFunction linkFunction;
  private Actions actions;

  public RedmineWorkflowBuilder(Actions actions, RedmineLinkFunction linkFunction) {
    this.actions = actions;
    this.linkFunction = linkFunction;
  }

  public void start() {

    actions
        .add(RedmineConstants.LINK_TO_REDMINE_ID)
        .setConditions(new NotCondition(new HasIssuePropertyCondition(RedmineConstants.ISSUE_ID)), new IsUnResolved())
        .setFunctions(linkFunction);
  }
}
