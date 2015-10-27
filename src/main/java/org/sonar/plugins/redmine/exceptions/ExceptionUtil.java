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
package org.sonar.plugins.redmine.exceptions;

import com.taskadapter.redmineapi.NotAuthorizedException;
import com.taskadapter.redmineapi.NotFoundException;

public class ExceptionUtil {

  public static com.taskadapter.redmineapi.RedmineException wrapException(Exception e) {
    // Work around to be able to catch exceptions in ruby on rails controller
    // It seems not to be possible to catch exceptions which aren't part of
    // this plugin
    com.taskadapter.redmineapi.RedmineException ex;
    if (e instanceof com.taskadapter.redmineapi.RedmineAuthenticationException) {
      ex = new org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException(e.getMessage());
    } else if (e instanceof com.taskadapter.redmineapi.RedmineTransportException) {
      ex = new org.sonar.plugins.redmine.exceptions.RedmineTransportException(e.getMessage());
    } else if (e instanceof NotFoundException) {
      ex = new RedmineNotFoundException(e.getMessage());
    } else if (e instanceof NotAuthorizedException) {
      ex = new RedmineNotAuthorizedException(e.getMessage());
    } else if (e instanceof com.taskadapter.redmineapi.RedmineException) {
      ex = new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getMessage());
    } else {
      if (e.getCause() == null) {
        ex = new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getLocalizedMessage());
      } else {
        ex = new org.sonar.plugins.redmine.exceptions.RedmineGeneralException(e.getCause().getLocalizedMessage());
      }
    }

    ex.initCause(e);
    return ex;
  }

}
