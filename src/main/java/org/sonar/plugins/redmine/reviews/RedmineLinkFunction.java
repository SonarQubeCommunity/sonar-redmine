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

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import org.sonar.api.ServerExtension;
import org.sonar.api.i18n.I18n;
import org.sonar.api.issue.action.Function;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

import java.util.Locale;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineLinkFunction implements Function, ServerExtension {
  private final RedmineAdapter redmineAdapter;
  private final RedmineIssueFactory issueFactory;
  private final I18n i18n;

  public RedmineLinkFunction(RedmineIssueFactory issueFactory, RedmineAdapter redmineAdapter, I18n i18n) {
    this.redmineAdapter = redmineAdapter;
    this.issueFactory = issueFactory;
    this.i18n = i18n;
  }

  @Override
  public void execute(Context context) {
    try {
      RedmineSettings redmineSettings = new RedmineSettings(context.projectSettings());

      Issue issue = issueFactory.createRedmineIssue(context.issue(), redmineSettings);
      redmineAdapter.connectToHost(redmineSettings.getHost(), redmineSettings.getApiAccessKey());
      issue = redmineAdapter.createIssue(redmineSettings.getProjectKey(), issue);

      context.addComment(generateCommentText(issue, redmineSettings));
      context.setAttribute(RedmineConstants.ISSUE_ID, issue.getId().toString());
    } catch (RedmineException ex) {
      throw new IllegalStateException(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_REMOTE_SERVER_ERROR, null) + ex.getMessage(),
          ex);
    }
  }

  protected String generateCommentText(Issue issue, RedmineSettings redmineSettings) {
    StringBuilder message = new StringBuilder();

    message.append(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_COMMENT, null));
    message.append(redmineSettings.getHost());
    message.append("/issues/");
    message.append(issue.getId().toString());

    return message.toString();
  }
}
