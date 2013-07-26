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

import org.junit.Test;
import org.sonar.api.workflow.Workflow;
import org.sonar.api.workflow.condition.Condition;
import org.sonar.api.workflow.screen.Screen;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.sonar.plugins.redmine.RedmineLanguageConstants;

public class RedmineWorkflowBuilderTest {

  @Test
  public void checkStart() throws Exception {
    Workflow workflow = mock(Workflow.class);
    RedmineLinkFunction function = mock(RedmineLinkFunction.class);

    RedmineWorkflowBuilder builder = new RedmineWorkflowBuilder(workflow, function);
    builder.start();

    verify(workflow, times(1)).addCommand(RedmineLanguageConstants.LINK_TO_REDMINE_ID);
    verify(workflow, times(1)).setScreen(anyString(), any(Screen.class));
    verify(workflow, times(1)).addFunction(RedmineLanguageConstants.LINK_TO_REDMINE_ID, function);
    verify(workflow, times(7)).addCondition(anyString(), any(Condition.class));
  }

}
