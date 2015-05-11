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
 * Abstract class, parent of the all formatter class.
 */
public abstract class TextileFormatter {

  protected Element element;

  protected abstract String toTextile();

  /**
   * Return true if the element don't have other element like children.
   */
  protected boolean isBaseCase(Element e) {
    boolean res = true;
    for (Node n : e.childNodes()) {
      if (n instanceof Element) {
        res = false;
      }
    }
    return res;
  }
}
