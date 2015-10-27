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
 *
 * <ul>
 *  <li><code>private</code>: Never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger.</li>
 *  <li><code>static</code>: Not dependent on an instance of a class (an object). When logging something, contextual information can of course be provided in the messages but the logger should be created at class level to prevent creating a logger along with each object.</li>
 *  <li><code>final</code>: Be created once and only once per class.</li>
 * </ul>
 *
 * To:
 * @private@: Never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger.\n
 * @static@: Not dependent on an instance of a class (an object). When logging something, contextual information can of course be provided in the messages but the logger should be created at class level to prevent creating a logger along with each object.\n
 * @final@: Be created once and only once per class.\n\n
 */
public class TextileFormatterUl extends TextileFormatter {

  public TextileFormatterUl(Element e) {
    this.element = e;
  }

  @Override
  protected String toTextile() {
    StringBuilder res = new StringBuilder();
    for (Element li : element.getAllElements()) {
      if (li.tagName().equals(TagEnum.liTag.toString())) {
        res.append("* ");
        res.append(UtilsFormat.callConcreteFormatter(li).toTextile());
        res.append("\n");
      }
    }
    res.append("\n");

    return res.toString();
  }

}
