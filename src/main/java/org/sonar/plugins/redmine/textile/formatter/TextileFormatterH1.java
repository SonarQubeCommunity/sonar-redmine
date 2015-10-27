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

import org.jsoup.nodes.Element;

/**
 * Parse this:
 * <h1>Noncompliant Code Example</h1>
 *
 * To:
 * h1. Noncompliant Code Example/n/n
 */
public class TextileFormatterH1 extends TextileFormatter {

  public TextileFormatterH1(Element e) {
    this.element = e;
  }

  @Override
  protected String toTextile() {
    StringBuilder res = new StringBuilder();
    res.append("h1. ");
    res.append(element.ownText());
    res.append("\n\n");

    return res.toString();
  }

}
