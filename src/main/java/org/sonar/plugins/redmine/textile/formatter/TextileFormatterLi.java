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

import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;

/**
 * Parse this:
 *
 * <li><code>private</code>: Never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger.</li>
 *
 * To:
 * @private@: Never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger.\n
 */
public class TextileFormatterLi extends TextileFormatter {

  public TextileFormatterLi(Element e) {
    this.element = e;
  }

  @Override
  protected String toTextile() {
    StringBuilder res = new StringBuilder();
    if (isBaseCase(element)) {
      for (Node n : element.childNodes()) {
        res.append(n.toString());
      }
    } else {
      for (Node n : element.childNodes()) {
        if (!(n instanceof Element)) {
          res.append(n.toString());
        } else {
          if (UtilsFormat.support(((Element) n))) {
            res.append(UtilsFormat.callConcreteFormatter((Element) n).toTextile());
          }
        }
      }
    }
    return res.toString();
  }

}
