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

import org.sonar.api.ServerExtension;

import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;

public class RedmineIssueFactory implements ServerExtension{
  
  public RedmineIssueFactory (){
  }
  
  public Issue createRemineIssue(String subject, String description) {
    Issue issue = new Issue();
    issue.setTracker(new Tracker(2, "Feature"));
    issue.setPriorityId(2);
    issue.setSubject(subject);
    issue.setDescription(description);
    return issue;
  }
  
}