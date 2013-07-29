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
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import java.util.Locale;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.i18n.I18n;
import org.sonar.api.issue.action.Function.Context;
import org.sonar.api.issue.action.Function;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.batch.RedmineSettings;
import org.sonar.plugins.redmine.client.RedmineAdapter;

public class RedmineLinkFunction implements Function, ServerExtension {

  private final RedmineAdapter redmineAdapter;
  private final RedmineIssueFactory issueFactory;
  private final I18n i18n;
  private RedmineSettings redmineSettings;
  protected RedmineManager redmineMgr;

  public RedmineLinkFunction(RedmineIssueFactory issueFactory, 
                            RedmineAdapter redmineAdapter, 
                            I18n i18n) {
    this.redmineAdapter = redmineAdapter;
    this.issueFactory = issueFactory;
    this.i18n = i18n;
  }

  public void execute(Context context) {
      checkConditions(context.projectSettings());
      try {
          createRedmineIssue(context);
    } catch (RedmineException e) {
        e.printStackTrace();
   }
  }
  private void createRedmineIssue(Context context) throws RedmineException{
      Issue issue = issueFactory.createRedmineIssue(context.issue(),context.projectSettings());
      redmineSettings = new RedmineSettings(context.projectSettings());
      redmineAdapter.connectToHost(redmineSettings.getHost(), redmineSettings.getApiAccessKey());
      issue = redmineAdapter.createIssue(redmineSettings.getProjectKey(), issue);
      createComment(issue,context);
      
     
  }
  private void checkConditions(Settings settings) {
      checkProperty(RedmineConstants.API_ACCESS_KEY, settings);
      checkProperty(RedmineConstants.PROJECT_KEY, settings);
      checkProperty(RedmineConstants.HOST, settings);
  }
  private void checkProperty(String property, Settings settings){
      if (!settings.hasKey(property) && !settings.hasDefaultValue(property)) {
	      throw new IllegalStateException("The Redmine property \"" + property + "\" must be defined before you can use the \"Link to Redmine\" button");
	    }
	 
 }

  protected void createComment(Issue issue,Context context) {
      context.addComment(generateCommentText(issue));
  }

  protected String generateCommentText(Issue issue) {
      StringBuilder message = new StringBuilder();
      message.append(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_COMMENT, null));
      message.append(redmineSettings.getHost());
      message.append("/issues/");
      message.append(issue.getId().toString());
      return message.toString();
  }

}
