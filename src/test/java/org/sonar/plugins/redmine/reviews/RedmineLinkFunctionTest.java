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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.config.Settings;
import org.sonar.api.workflow.Comment;
import org.sonar.api.workflow.MutableReview;
import org.sonar.api.workflow.Review;
import org.sonar.api.workflow.WorkflowContext;
import org.sonar.plugins.redmine.RedmineLanguageConstants;

import java.util.HashMap;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import org.sonar.api.i18n.I18n;
import org.sonar.plugins.redmine.RedminePlugin;
import org.sonar.plugins.redmine.client.RedmineAdapter;
import org.sonar.plugins.redmine.config.RedmineSettings;

public class RedmineLinkFunctionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private RedmineAdapter redmineAdapter;
  private RedmineIssueFactory redmineIssueFactory;
  private RedmineLinkFunction action;
  private String projectKey = "prKey";
  private MutableReview mutableReview;
  private Comment comment;
  private Review review;
  private WorkflowContext workflowContext;
  private Issue redmineIssue;
  private Settings settings;
  private RedmineSettings redmineSettings;
  private I18n i18n;

  @Before
  public void setUpMocks() throws Exception {
    settings = new Settings();
    settings.setProperty(RedmineSettings.API_ACCESS_KEY, "api_access_key");
    settings.setProperty(RedmineSettings.HOST, "http://my.Redmine.server");
    settings.setProperty(RedmineSettings.PROJECT_KEY, projectKey);
    redmineSettings = new RedmineSettings(settings);
    
    redmineIssue = new Issue();
    redmineIssue.setId(10);

    i18n = mock(I18n.class);
    when(i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_COMMENT, null)).thenReturn("Review linked to Redmine issue: ");
    when(i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_REMOTE_SERVER_ERROR, null)).thenReturn("Impossible to create an issue on Redmine. A problem occured with the remote server: ");

    comment = mock(Comment.class);

    mutableReview = mock(MutableReview.class);
    when(mutableReview.createComment()).thenReturn(comment);

    review = mock(Review.class);
    
    workflowContext = mock(WorkflowContext.class);
    when(workflowContext.getProjectSettings()).thenReturn(settings);
    
    redmineIssueFactory = mock(RedmineIssueFactory.class);
    when(redmineIssueFactory.createRemineIssue(review.getMessage(), review.getRuleName())).thenReturn(redmineIssue);
    
    redmineAdapter = mock(RedmineAdapter.class);
    doNothing().when(redmineAdapter).connectToHost("http://my.Redmine.server", "api_access_key");
    when(redmineAdapter.createIssue(projectKey, redmineIssue)).thenReturn(redmineIssue);

    action = new RedmineLinkFunction(redmineIssueFactory,redmineAdapter,i18n);
  }

  @Test
  public void shouldExecute() throws Exception {
    
    action.doExecute(mutableReview, review, workflowContext, new HashMap<String, String>());

    verify(redmineIssueFactory).createRemineIssue(review.getMessage(), review.getRuleName());
    verify(redmineAdapter).createIssue(projectKey, redmineIssue);
    verify(mutableReview).createComment();
    verify(mutableReview).setProperty(RedmineLanguageConstants.ISSUE_ID, "10");
  }

  @Test
  public void shouldFailExecuteIfRemoteProblem() throws Exception {
    when(redmineAdapter.createIssue(projectKey,redmineIssue)).thenThrow(new RedmineException("Server Error"));

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Impossible to create an issue on Redmine. A problem occured with the remote server: Server Error");

    action.doExecute(mutableReview, review, workflowContext, new HashMap<String, String>());
  }

  @Test
  public void testCreateComment() throws Exception {
    when(workflowContext.getUserId()).thenReturn(45L);
    action.createComment(redmineIssue, mutableReview, workflowContext, new HashMap<String, String>());

    verify(comment).setUserId(45L);
    verify(comment).setMarkdownText("Review linked to Redmine issue: http://my.Redmine.server/issues/10");
  }

  @Test
  public void testGenerateCommentText() throws Exception {
    final String userComment = "This is a user comment";
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("text", userComment);

    String commentText = action.generateCommentText(redmineIssue, redmineSettings, params);
    assertThat(commentText).isEqualTo(userComment + "\n\nReview linked to Redmine issue: http://my.Redmine.server/issues/10");
  }

}
