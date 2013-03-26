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
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.ServerExtension;
import org.sonar.api.i18n.I18n;
import org.sonar.api.resources.File;
import org.sonar.api.workflow.Comment;
import org.sonar.api.workflow.MutableReview;
import org.sonar.api.workflow.Review;
import org.sonar.api.workflow.WorkflowContext;
import org.sonar.api.workflow.function.Function;
import org.sonar.plugins.redmine.RedmineLanguageConstants;
import org.sonar.plugins.redmine.client.RedmineAdapter;
import org.sonar.plugins.redmine.config.RedmineSettings;

public class RedmineLinkFunction extends Function implements ServerExtension {

	private final RedmineAdapter redmineAdapter;
	private final RedmineIssueFactory issueFactory;
	private final I18n i18n;

	public RedmineLinkFunction(RedmineIssueFactory issueFactory, RedmineAdapter redmineAdapter, I18n i18n) {
		this.redmineAdapter = redmineAdapter;
		this.issueFactory = issueFactory;
		this.i18n = i18n;
	}

	@Override
	public void doExecute(MutableReview review, Review initialReview, WorkflowContext context, Map<String, String> parameters) {
		try {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, String> entry : parameters.entrySet()) {
				sb.append(entry.getKey());
				sb.append("-> ");
				sb.append(entry.getValue());
				sb.append("\n");
			}

			RedmineSettings redmineSettings = new RedmineSettings(context.getProjectSettings());

			Issue issue = issueFactory.createRemineIssue(review, redmineSettings, parameters);
			redmineAdapter.connectToHost(redmineSettings.getHost(), redmineSettings.getApiAccessKey());
			issue = redmineAdapter.createIssue(redmineSettings.getProjectKey(), issue);

			createComment(issue, review, context, parameters);
			review.setProperty(RedmineLanguageConstants.ISSUE_ID, issue.getId().toString());
		} catch (RedmineException ex) {
			throw new IllegalStateException(i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_REMOTE_SERVER_ERROR, null)
					+ ex.getMessage(), ex);
		}
	}

	protected void createComment(Issue issue, MutableReview review, WorkflowContext context, Map<String, String> parameters) {
		Comment newComment = review.createComment();
		newComment.setUserId(context.getUserId());
		newComment.setMarkdownText(generateCommentText(issue, new RedmineSettings(context.getProjectSettings()), parameters));
	}

	protected String generateCommentText(Issue issue, RedmineSettings redmineSettings, Map<String, String> parameters) {
		StringBuilder message = new StringBuilder();
		String text = parameters.get("text");
		if (!StringUtils.isBlank(text)) {
			message.append(text);
			message.append("\n\n");
		}
		message.append(i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_COMMENT, null));
		message.append(redmineSettings.getHost());
		message.append("/issues/");
		message.append(issue.getId().toString());
		return message.toString();
	}

}
