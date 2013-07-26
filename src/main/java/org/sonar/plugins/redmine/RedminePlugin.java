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

import com.google.common.collect.ImmutableList;
import com.taskadapter.redmineapi.RedmineManager;

import java.util.List;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.plugins.redmine.batch.RedmineSensor;
import org.sonar.plugins.redmine.batch.RedmineSettings;
import org.sonar.plugins.redmine.client.RedmineAdapter;
import org.sonar.plugins.redmine.reviews.RedmineIssueFactory;
import org.sonar.plugins.redmine.reviews.RedmineLinkFunction;
import org.sonar.plugins.redmine.reviews.RedmineActionDefinition;
import org.sonar.plugins.redmine.ui.RedmineWidget;

@Properties({
  @Property(key = RedmineConstants.HOST,
            name = "Redmine Host URL",
            description = "Example : http://demo.redmine.org/",
            global = true,
            defaultValue = "",
            project = true,
            module = false),
  @Property(key = RedmineConstants.API_ACCESS_KEY,
            name = "API Access Key",
            description = "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.",
            type = org.sonar.api.PropertyType.PASSWORD,
            global = true,
            project = true,
            module = false),
  @Property(key = RedmineConstants.PROJECT_KEY,
            name = "Project Key",
            global = false,
            project = true,
            module = false)
})
public class RedminePlugin extends SonarPlugin {

  private RedmineManager redmineMgr;

public List getExtensions() {
    return ImmutableList.of(
    		 //Settings Properties
            PropertyDefinition.builder(RedmineConstants.REDMINE_INFO_PRIORITY_ID)
            .name("Redmine priority id for INFO")
            .type(PropertyType.SINGLE_SELECT_LIST)
            .description("Redmine priority id used to create issues for Sonar violations with severity INFO.")
            .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getPriorityValueFromRedmine(redmineMgr)))
            .build(),

            PropertyDefinition.builder(RedmineConstants.REDMINE_MINOR_PRIORITY_ID)
            .name("Redmine priority id for MINOR")
            .type(PropertyType.SINGLE_SELECT_LIST)
            .description("Redmine priority id used to create issues for Sonar violations with severity MINOR.")
            .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getPriorityValueFromRedmine(redmineMgr)))
            .build(),

            PropertyDefinition.builder(RedmineConstants.REDMINE_MAJOR_PRIORITY_ID)
            .name("Redmine priority id for MAJOR")
            .type(PropertyType.SINGLE_SELECT_LIST)
            .description("Redmine priority id used to create issues for Sonar violations with severity MAJOR.")
            .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getPriorityValueFromRedmine(redmineMgr)))
            .build(),

            PropertyDefinition.builder(RedmineConstants.REDMINE_CRITICAL_PRIORITY_ID)
            .name("Redmine priority id for CRITICAL")
            .type(PropertyType.SINGLE_SELECT_LIST)
            .description("Redmine priority id used to create issues for Sonar violations with severity CRITICAL.")
            .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getPriorityValueFromRedmine(redmineMgr)))
            .build(),

            PropertyDefinition.builder(RedmineConstants.REDMINE_BLOCKER_PRIORITY_ID)
           .name("Redmine priority id for BLOCKER")              
            .type(PropertyType.SINGLE_SELECT_LIST)
           .description("Redmine priority id used to create issues for Sonar violations with severity BLOCKER.")
            .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getPriorityValueFromRedmine(redmineMgr)))
            .build(),

           PropertyDefinition.builder(RedmineConstants.REDMINE_ISSUE_TYPE)
           .name("Type of Redmine issue")
           .type(PropertyType.SINGLE_SELECT_LIST)
            .description("Redmine issue type used to create issues for Sonar violations. Default is Feature.")
           .options(RedmineProperty.convertObjListToStrList(RedmineProperty.getIssueTypeFromRedmine(redmineMgr)))
            .build(),
            // Definitions
            RedmineMetrics.class,
            // Batch
            RedmineSensor.class,RedmineAdapter.class,RedmineSettings.class,
            // Server
            RedmineIssueFactory.class,
            // UI
            RedmineWidget.class,
            // Reviews
            RedmineLinkFunction.class,RedmineActionDefinition.class
            );
  }
}