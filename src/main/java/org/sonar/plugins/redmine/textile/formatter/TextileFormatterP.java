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
import org.jsoup.nodes.Node;

/**
 * Parse this:
 * <p>The Cyclomatic Complexity is measured by the number of
 *  (&amp;&amp;, ||) operators and (if, while, do, for, ?:, catch, switch,
 *  case, return, throw) statements in the body of a class plus one for
 *  each constructor, method (but not getter/setter), static initializer,
 *  or instance initializer in the class. The last return stament in
 *  method, if exists, is not taken into account.</p>
 *
 *  To:
 *  The Cyclomatic Complexity is measured by the number of
 *  (&amp;&amp;, ||) operators and (if, while, do, for, ?:, catch, switch,
 *  case, return, throw) statements in the body of a class plus one for
 *  each constructor, method (but not getter/setter), static initializer,
 *  or instance initializer in the class. The last return stament in
 *  method, if exists, is not taken into account.\n\n
 */
public class TextileFormatterP extends TextileFormatter {

  public TextileFormatterP(Element e) {
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

    return formatWhiteSpace(res);
  }

  /**
   * Delete the first character in the case that is a white space.
   */
  private String formatWhiteSpace(StringBuilder string) {
    if (string.toString().startsWith(" ")) {
      string.deleteCharAt(0);
    }
    if (string.toString().endsWith(" ")){
      string.deleteCharAt(string.length()-1);
    }
    string.append("\n\n");
    return string.toString();
  }

}
