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
package org.sonar.plugins.redmine.client;

import com.google.common.collect.Maps;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import java.util.List;
import java.util.Map;
import org.sonar.api.BatchExtension;

public class RedmineAdapter implements BatchExtension {

  protected RedmineManager redmineMgr;
  
  public final void connectToHost (final String host, final String apiKey){
    redmineMgr = new RedmineManager(host, apiKey);
  }
  
  public final Map<String, Integer> collectProjectIssuesByPriority(
          final String projectKey) 
          throws RedmineException{
    
    List<Issue> issues = redmineMgr.getIssues(projectKey, null);
    Map<String, Integer> issuesByPriority = Maps.newHashMap();

    for (Issue issue : issues) {
      String priority = issue.getPriorityText();
      if (!issuesByPriority.containsKey(priority)) {
        issuesByPriority.put(priority, 1);
      } else {
        issuesByPriority.put(priority, issuesByPriority.get(priority) + 1);
      }
    }
    return issuesByPriority;
  }
}