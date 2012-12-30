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
import java.util.List;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.redmine.batch.RedmineSensor;
import org.sonar.plugins.redmine.client.RedmineAdapter;
import org.sonar.plugins.redmine.reviews.RedmineLinkFunction;
import org.sonar.plugins.redmine.reviews.RedmineWorkflowBuilder;
import org.sonar.plugins.redmine.ui.RedmineWidget;

@Properties({
  @Property(key = RedminePlugin.HOST,
            name = "Redmine Host URL",
            description = "Example : http://demo.redmine.org/",
            global = true,
            defaultValue = "",
            project = true,
            module = false),
  @Property(key = RedminePlugin.API_ACCESS_KEY,
            name = "API Access Key",
            description = "You can find your API key on your account page ( /my/account ) when logged in, on the right-hand pane of the default layout.",
            type = org.sonar.api.PropertyType.PASSWORD,
            global = true,
            project = true,
            module = false),
  @Property(key = RedminePlugin.PROJECT_KEY,
            name = "Project Key",
            global = false,
            project = true,
            module = false)
})
public final class RedminePlugin extends SonarPlugin {

  public static final String HOST = "sonar.redmine.host";
  public static final String API_ACCESS_KEY = "sonar.redmine.api-access-key";
  public static final String PROJECT_KEY = "sonar.redmine.project-key";

  public List getExtensions() {
    return ImmutableList.of(
            // Definitions
            RedmineMetrics.class,
            // Batch
            RedmineSensor.class,RedmineAdapter.class,
            // UI
            RedmineWidget.class,
            // Reviews
            RedmineLinkFunction.class,RedmineWorkflowBuilder.class
            );
  }
}