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

import com.google.common.collect.ImmutableList;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedmineAdapterTest {
 
  @Test
  public void shouldCollectProjectIssuesByPriority() throws Exception {
    
    final RedmineAdapter redmineAdapter = new RedmineAdapter();
    
    final String projectKey = "prKey";
    RedmineManager mockedRedmineMgr = mock(RedmineManager.class);
    
    Issue issue1 = new Issue();
    issue1.setPriorityText("Normal");
    Issue issue2 = new Issue();
    issue2.setPriorityText("Normal");
    Issue issue3 = new Issue();
    issue3.setPriorityText("Urgent");
    when(mockedRedmineMgr.getIssues(projectKey, null)).thenReturn(ImmutableList.of(issue1, issue2, issue3));

    redmineAdapter.redmineMgr = mockedRedmineMgr;
    Map<String, Integer> collectedIssues = redmineAdapter.collectProjectIssuesByPriority(projectKey);
    assertThat(collectedIssues.size(), is(2));
    assertThat(collectedIssues.get("Normal"),is(2));
    assertThat(collectedIssues.get("Urgent"),is(1));  
  }
}
