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
package org.sonar.plugins.redmine;

public final class RedmineConstants {

  private RedmineConstants() {
  }

  public static final String LINK_TO_REDMINE_ID = "link-to-redmine";

  // Review data ids
  public static final String ISSUES_DOMAIN = "Issues";
  public static final String ISSUES_KEY = "redmine-issues";
  public static final String ISSUE_ID = "redmine-issue-id";

  // Metric data ids
  public static final String ISSUES_BY_PRIORITY_KEY = "redmine-issues-by-priority";
  public static final String ISSUES_BY_DEVELOPER_KEY = "redmine-issues-by-developer";
  public static final String ISSUES_BY_STATUS_KEY = "redmine-issues-by-status";

  // Language string ids
  public static final String LINKED_ISSUE_SUBJECT_TEMPLATE = "redmine.linked_issue.subject.template";
  public static final String LINKED_ISSUE_SUBJECT_TEMPLATE_NO_RULE = "redmine.linked_issue.subject.template.no_rule";
  public static final String LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITH_MESSAGE = "redmine.linked_issue.description.template.with_message";
  public static final String LINKED_ISSUE_DESCRIPTION_TEMPLATE_WITHOUT_MESSAGE = "redmine.linked_issue.description.template.without_message";

  public static final String LINKED_ISSUE_COMMENT = "redmine.linked_issue.comment";
  public static final String LINKED_ISSUE_REMOTE_SERVER_ERROR = "redmine.linked_issue.remote_server_error";

  public static final String CONFIGURATION_PAGE = "page.redmine_configuration.name";
}
