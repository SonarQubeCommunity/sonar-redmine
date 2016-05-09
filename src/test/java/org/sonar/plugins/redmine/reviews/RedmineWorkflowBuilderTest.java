/*
 * SonarQube Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU and Christian Schulz
 * sonarqube@googlegroups.com
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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.issue.action.Actions;
import static org.mockito.Mockito.mock;
import static org.fest.assertions.api.Assertions.*;
import org.sonar.api.issue.action.Action;

public class RedmineWorkflowBuilderTest {

  private RedmineWorkflowBuilder workflowBuilder;
  private RedmineLinkFunction linkFunction;
  private final Actions actions = new Actions();

  
  @Before
  public void setUp() throws Exception {
    linkFunction = mock(RedmineLinkFunction.class);
    workflowBuilder = new RedmineWorkflowBuilder(actions, linkFunction);
  }

  @Test
  public void shouldStart() {
    workflowBuilder.start();
    
    Action action = actions.list().get(0);
    assertThat(action.key()).isEqualTo("link-to-redmine");
    assertThat(action.functions().get(0)).isEqualTo(linkFunction);
    assertThat(action.conditions()).isNotEmpty();    
    
  }

}
