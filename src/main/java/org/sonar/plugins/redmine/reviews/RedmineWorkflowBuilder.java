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
package org.sonar.plugins.redmine.reviews;

import static org.sonar.api.workflow.condition.Conditions.hasProjectProperty;
import static org.sonar.api.workflow.condition.Conditions.hasReviewProperty;
import static org.sonar.api.workflow.condition.Conditions.not;
import static org.sonar.api.workflow.condition.Conditions.statuses;

import org.sonar.api.ServerExtension;
import org.sonar.api.workflow.Workflow;
import org.sonar.api.workflow.screen.CommentScreen;
import org.sonar.plugins.redmine.RedmineLanguageConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

public class RedmineWorkflowBuilder implements ServerExtension {

  private final Workflow workflow;
  private final RedmineLinkFunction linkFunction;

  public RedmineWorkflowBuilder(Workflow workflow, RedmineLinkFunction linkFunction) {
    this.workflow = workflow;
    this.linkFunction = linkFunction;
  }

  public void start() {
    workflow.addCommand(RedmineLanguageConstants.LINK_TO_REDMINE_ID);
    workflow.setScreen(RedmineLanguageConstants.LINK_TO_REDMINE_ID, new CommentScreen());
    workflow.addFunction(RedmineLanguageConstants.LINK_TO_REDMINE_ID, linkFunction);
    // conditions for this function
    // - on the review ("IDLE" is the non-persisted status of an non-existing review = when a violation does have a review yet)
    workflow.addCondition(RedmineLanguageConstants.LINK_TO_REDMINE_ID, not(hasReviewProperty(RedmineLanguageConstants.ISSUE_ID)));
    workflow.addCondition(RedmineLanguageConstants.LINK_TO_REDMINE_ID, statuses("IDLE", "OPEN", "REOPENED"));
    // - on the project
    workflow.addCondition(RedmineLanguageConstants.LINK_TO_REDMINE_ID, hasProjectProperty(RedmineSettings.HOST));
    workflow.addCondition(RedmineLanguageConstants.LINK_TO_REDMINE_ID, hasProjectProperty(RedmineSettings.API_ACCESS_KEY));
    workflow.addCondition(RedmineLanguageConstants.LINK_TO_REDMINE_ID, hasProjectProperty(RedmineSettings.PROJECT_KEY));
  }
}
