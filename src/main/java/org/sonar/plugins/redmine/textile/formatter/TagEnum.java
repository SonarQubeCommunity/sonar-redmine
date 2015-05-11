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

/**
 * Enum used for registred the tags support for the method.
 */
public enum TagEnum {

  pTag("p"),
  aTag("a"),
  ulTag("ul"),
  liTag("li"),
  codeTag("code"),
  preTag("pre"),
  h1Tag("h1"),
  h2Tag("h2"),
  h3Tag("h3");

  private String result;

  private TagEnum(String result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return result;
  }

  public static TagEnum stringToEnum(String source) {
    TagEnum e;
    if (pTag.toString().equals(source)) {
      e = pTag;
    } else if (aTag.toString().equals(source)) {
      e = aTag;
    } else if (ulTag.toString().equals(source)) {
      e = ulTag;
    } else if (liTag.toString().equals(source)) {
      e = liTag;
    } else if (codeTag.toString().equals(source)) {
      e = codeTag;
    } else if (preTag.toString().equals(source)) {
      e = preTag;
    } else if (h1Tag.toString().equals(source)) {
      e = h1Tag;
    } else if (h2Tag.toString().equals(source)) {
      e = h2Tag;
    } else if (h3Tag.toString().equals(source)) {
      e = h3Tag;
    } else {
      e = null;
    }
    return e;
  }

}
