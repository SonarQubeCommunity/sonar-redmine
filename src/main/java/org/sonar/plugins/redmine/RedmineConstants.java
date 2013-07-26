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
package org.sonar.plugins.redmine;

public final class RedmineConstants {

  private RedmineConstants() {
  }
  public static final String HOST = "sonar.redmine.host";
  public static final String API_ACCESS_KEY = "sonar.redmine.api-access-key";
  public static final String PROJECT_KEY = "sonar.redmine.project-key";
  
  public static final String REDMINE_INFO_PRIORITY_ID = "sonar.redmine.info.priority.id";
  public static final String REDMINE_MINOR_PRIORITY_ID = "sonar.redmine.minor.priority.id";
  public static final String REDMINE_MAJOR_PRIORITY_ID = "sonar.redmine.major.priority.id";
  public static final String REDMINE_CRITICAL_PRIORITY_ID = "sonar.redmine.critical.priority.id";
  public static final String REDMINE_BLOCKER_PRIORITY_ID = "sonar.redmine.blocker.priority.id";
  public static final String REDMINE_ISSUE_TYPE_ID = "sonar.redmine.issue.type.id";
  public static final String REDMINE_ISSUE_TYPE = "sonar.redmine.issue.type";
  
  public static final String ISSUES_DOMAIN = "Issues";
  public static final String ISSUES_KEY = "redmine-issues";
  public static final String ISSUES_BY_PRIORITY_KEY = "redmine-issues-by-priority";
  public static final String ISSUE_ID = "redmine-issue-id";
  public static final String LINKED_ISSUE_SUBJECT = "redmine.linked_issue.subject";
  public static final String LINKED_ISSUE_DESCRIPTION = "redmine.linked_issue.description";
  public static final String LINKED_ISSUE_COMMENT = "redmine.linked_issue.comment";
  public static final String LINKED_ISSUE_REMOTE_SERVER_ERROR = "redmine.linked_issue.remote_server_error";
  public static final String LINK_TO_REDMINE_ID = "link-to-redmine";
   
}
