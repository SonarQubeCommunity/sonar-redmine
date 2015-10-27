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
import com.taskadapter.redmineapi.RedmineException;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;
public class ExceptionUtilTest {

  private static final String MESSAGE = "message";

  @Test
  public void shouldWrapRedmineAuthenticationException() {
    Exception ex = ExceptionUtil.wrapException(new com.taskadapter.redmineapi.RedmineAuthenticationException(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineAuthenticationException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapRedmineTransportException() {
    Exception ex = ExceptionUtil.wrapException(new com.taskadapter.redmineapi.RedmineTransportException(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineTransportException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapNotFoundException() {
    Exception ex = ExceptionUtil.wrapException(new NotFoundException(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineNotFoundException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapNotAuthorizedException() {
    Exception ex = ExceptionUtil.wrapException(new NotAuthorizedException(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineNotAuthorizedException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapRedmineException() {
    Exception ex = ExceptionUtil.wrapException(new RedmineException(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineGeneralException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapExceptionWithoutCause() {
    Exception ex = ExceptionUtil.wrapException(new Exception(MESSAGE));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineGeneralException.class).hasMessage(MESSAGE);
  }

  @Test
  public void shouldWrapExceptionWithCause() {
    Exception ex = ExceptionUtil.wrapException(new Exception(new Throwable(MESSAGE)));
    assertThat(ex).isInstanceOf(org.sonar.plugins.redmine.exceptions.RedmineGeneralException.class).hasMessage(MESSAGE);
  }
}
