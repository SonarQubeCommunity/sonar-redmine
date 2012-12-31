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

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.workflow.Comment;
import org.sonar.api.workflow.MutableReview;
import org.sonar.api.workflow.Review;
import org.sonar.api.workflow.WorkflowContext;
import org.sonar.api.workflow.function.Function;
import org.sonar.plugins.redmine.RedminePlugin;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineLinkFunction extends Function implements ServerExtension {

  private final RedmineAdapter redMineAdapter;

  public RedmineLinkFunction(RedmineAdapter redMineAdapter) {
    this.redMineAdapter = redMineAdapter;
  }

  @Override
  public void doExecute(MutableReview review, Review initialReview, WorkflowContext context, Map<String, String> parameters) {
    try {
      Settings settings = context.getProjectSettings();
      Issue issue = new Issue();
      issue.setTracker(new Tracker(2, "Feature"));
      issue.setPriorityId(2);
      issue.setSubject("Test Subject");
      issue.setDescription("Test Description");
      redMineAdapter.connectToHost(settings.getString(RedminePlugin.HOST), settings.getString(RedminePlugin.API_ACCESS_KEY));

      issue = redMineAdapter.createIssue(settings.getString(RedminePlugin.PROJECT_KEY), issue);
      createComment(issue, review, context, parameters);
      review.setProperty("redmine-issue-id", issue.getId().toString());
    } catch (RedmineException ex) {
      throw new IllegalStateException("Impossible to create an issue on Redmine. A problem occured with the remote server: " + ex.getMessage(), ex);
    }
  }

  protected void createComment(Issue issue, MutableReview review, WorkflowContext context, Map<String, String> parameters) {
    Comment newComment = review.createComment();
    newComment.setUserId(context.getUserId());
    newComment.setMarkdownText(generateCommentText(issue, context, parameters));
  }

  protected String generateCommentText(Issue issue, WorkflowContext context, Map<String, String> parameters) {
    StringBuilder message = new StringBuilder();
    String text = parameters.get("text");
    if (!StringUtils.isBlank(text)) {
      message.append(text);
      message.append("\n\n");
    }
    message.append("Review linked to Redmine issue: ");
    message.append(context.getProjectSettings().getString(RedminePlugin.HOST));
    message.append("/issues/");
    message.append(issue.getId().toString());
    return message.toString();
  }
}
