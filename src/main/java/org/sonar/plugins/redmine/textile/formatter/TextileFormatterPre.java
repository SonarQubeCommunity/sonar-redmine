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
package org.sonar.plugins.redmine.textile.formatter;

import org.apache.commons.lang.StringEscapeUtils;

import org.jsoup.nodes.Element;

/**
 * Parse this:
 * <pre>
 * public class Foo {
 *   public Logger logger = LoggerFactory.getLogger(Foo.class);         // Noncompliant
 *
 *   public void doSomething() {
 *   }
 * }
 *</pre>
 *
 * To:
 * \n<pre>
 * public class Foo {
 *   public Logger logger = LoggerFactory.getLogger(Foo.class);         // Noncompliant
 *
 *   public void doSomething() {
 *   }
 * }
 * </pre>\n\n
 */
public class TextileFormatterPre extends TextileFormatter {

  public TextileFormatterPre(Element e) {
    this.element = e;
  }

  @Override
  protected String toTextile() {
    StringBuilder res = new StringBuilder();
    res.append("<pre>\n");
    res.append(StringEscapeUtils.unescapeHtml(element.ownText()));
    res.append("\n</pre>\n\n");

    return res.toString();
  }

}
