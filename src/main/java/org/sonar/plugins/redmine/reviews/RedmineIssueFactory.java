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

import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;
import java.util.Locale;
import org.sonar.api.ServerExtension;
import org.sonar.api.i18n.I18n;
import org.sonar.plugins.redmine.RedmineConstants;

public class RedmineIssueFactory implements ServerExtension{
  
  private I18n i18n;
  public RedmineIssueFactory (I18n i18n){
    this.i18n = i18n;
  }
  
  public Issue createRemineIssue() {
    Issue issue = new Issue();
    issue.setTracker(new Tracker(2, "Feature"));
    issue.setPriorityId(2);
    issue.setSubject(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_SUBJECT, null));
    issue.setDescription(i18n.message(Locale.getDefault(), RedmineConstants.LINKED_ISSUE_DESCRIPTION, null));
    return issue;
  }
  
}