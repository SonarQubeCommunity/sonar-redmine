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

import java.util.Locale;
import java.util.Map;

import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

import org.sonar.api.CoreProperties;
import org.sonar.api.i18n.I18n;
import org.sonar.api.workflow.Review;
import org.sonar.plugins.redmine.RedmineLanguageConstants;
import org.sonar.plugins.redmine.config.RedmineSettings;

public class RedmineIssueFactory implements ServerExtension{
  private I18n i18n;

public RedmineIssueFactory (I18n i18n){
	    this.i18n = i18n;
  }
  
  public Issue createRemineIssue(Review review, RedmineSettings settings, Map<String, String> parameters) {
    Issue issue = new Issue();
    
    issue.setTracker(new Tracker(settings.getTrackerID(), null));
    issue.setPriorityId(settings.getPriorityID());
    issue.setSubject(createIssueSubject(review));
    issue.setDescription(createIssueDescription(review, settings.getString(CoreProperties.SERVER_BASE_URL), parameters.get("text")));
    
    return issue;
  }

  private String createIssueSubject(Review review) {
	return i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_SUBJECT_TEMPLATE, review.getReviewId(), review.getRuleName()));
  }
  
  private String createIssueDescription(Review review, String baseUrl, String comment) {
	StringBuilder sb = new StringBuilder();
	sb.append(baseUrl);
	sb.append("/project_reviews/view/");
	sb.append(review.getReviewId());
	  
	if (StringUtils.isNotBlank(comment)) {
		return i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITH_MESSAGE, review.getMessage(), comment, sb.toString()));  
	} else {
		return i18n.message(Locale.getDefault(), RedmineLanguageConstants.LINKED_ISSUE_SUBJECT_TEMPLATE, review.getMessage(), sb.toString()));  
	}
  }
}