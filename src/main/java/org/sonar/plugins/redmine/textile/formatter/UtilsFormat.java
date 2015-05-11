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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used for parse the description of the rule (in format HTML ) to textile.
 *
 * For add a new  support of the tag, you have add  first in TagEnum, after add a new formatter class that extend of
 * the RedmineFormatter and implement inside, the logic of the new Tag.
 *
 * Example of default format of rule description :
 *
 *  <p>The Cyclomatic Complexity is measured by the number of
 *  (&amp;&amp;, ||) operators and (if, while, do, for, ?:, catch, switch,
 *  case, return, throw) statements in the body of a class plus one <a
 *  href="http://en.wikipedia.org/wiki/Single_responsibility_principle">Single
 *  Responsibility Principle</a> for
 *  each constructor, method (but not getter/setter), static initializer,
 *  or instance initializer in the class. The last return stament in
 *  method, if exists, is not taken into account.</p>
 *  <ul>
 *  <li><code>private</code>: Never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger.</li>
 *  <li><code>static</code>: Not dependent on an instance of a class (an object). When logging something, contextual information can of course be provided in the messages but the logger should be created at class level to prevent creating a logger along with each object.</li>
 *  <li><code>final</code>: Be created once and only once per class.</li>
 *  <li><code>final</code>: <a href="http://en.wikipedia.org/wiki/Single_responsibility_principle">Single Responsibility Principle</a></li>
 *  </ul>
 *  <h2>Noncompliant Code Example</h2>
 *  <pre>
 *  public class Foo {
 *   public Logger logger = LoggerFactory.getLogger(Foo.class);         // Noncompliant
 *    public void doSomething() {
 *    }
 *  }
 *
 *  public class Bar {
 *   public static void main(String[] args) {
 *    Foo foo = new Foo();
 *    foo.logger.info("Program started");
 *    foo.doSomething();
 *   }
 *  }
 *  </pre>
 *  <a href="http://en.wikipedia.org/wiki/Single_responsibility_principle">Single Responsibility Principle</a>
 *
 */
public class UtilsFormat {

  public static String generateFormatToTextile(String html) {
    StringBuilder result = new StringBuilder();
    List<Element> firstLevel = getElementsFirstLevel(html);
    for (Element e : firstLevel) {
      if (support(e)) {
        TextileFormatter redmineFormat = callConcreteFormatter(e);
        result.append(redmineFormat.toTextile());
      }
    }
    return result.toString();
  }

  /**
   * Return the concrete implementation of the abstract class RedmineFormatter.
   */
  public static TextileFormatter callConcreteFormatter(Element e) {
    TextileFormatter redmineFormat = null;
    if (e.tagName().equals(TagEnum.pTag.toString())) {
      redmineFormat = new TextileFormatterP(e);
    } else if (e.tagName().equals(TagEnum.aTag.toString())) {
      redmineFormat = new TextileFormatterA(e);
    } else if (e.tagName().equals(TagEnum.codeTag.toString())) {
      redmineFormat = new TextileFormatterCode(e);
    } else if (e.tagName().equals(TagEnum.ulTag.toString())) {
      redmineFormat = new TextileFormatterUl(e);
    } else if (e.tagName().equals(TagEnum.liTag.toString())) {
      redmineFormat = new TextileFormatterLi(e);
    } else if (e.tagName().equals(TagEnum.preTag.toString())) {
      redmineFormat = new TextileFormatterPre(e);
    } else if (e.tagName().equals(TagEnum.h1Tag.toString())) {
      redmineFormat = new TextileFormatterH1(e);
    } else if (e.tagName().equals(TagEnum.h2Tag.toString())) {
      redmineFormat = new TextileFormatterH2(e);
    } else if (e.tagName().equals(TagEnum.h3Tag.toString())) {
      redmineFormat = new TextileFormatterH3(e);
    }
    return redmineFormat;
  }

  /**
   * Return true if the Element is soported by the method.
   */
  public static boolean support(Element e) {
    boolean res = e.tagName().equals(TagEnum.pTag.toString())
      || e.tagName().equals(TagEnum.aTag.toString())
      || e.tagName().equals(TagEnum.h1Tag.toString())
      || e.tagName().equals(TagEnum.h2Tag.toString())
      || e.tagName().equals(TagEnum.h3Tag.toString())
      || e.tagName().equals(TagEnum.preTag.toString())
      || e.tagName().equals(TagEnum.codeTag.toString())
      || e.tagName().equals(TagEnum.ulTag.toString())
      || e.tagName().equals(TagEnum.liTag.toString());
    return res;
  }

  /**
   * Return the first level of the elements given a element Body.
   * Only the first level, not the children of these.
   */
  private static List<Element> getElementsFirstLevel(String html) {
    Document docu = Jsoup.parse(html);
    List<Element> elements = new ArrayList<Element>();
    for (Node node : docu.getAllElements()) {
      if (node instanceof Element && node.parentNode() != null && node.parentNode().equals(docu.body())) {
        elements.add((Element) node);
      }
    }
    return elements;
  }
}
