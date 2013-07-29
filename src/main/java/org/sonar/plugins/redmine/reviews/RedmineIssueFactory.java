/*
 * Sonar Redmine Plugin
 * Copyright (C) 2013 Patroklos PAPAPETROU
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
package org.sonar.plugins.redmine.reviews;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Tracker;
import java.util.List;
import org.sonar.api.CoreProperties;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.i18n.I18n;
import org.sonar.plugins.redmine.RedmineConstants;
import org.sonar.plugins.redmine.RedmineProperty;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.utils.SonarException;
import com.taskadapter.redmineapi.bean.IssuePriority;

public class RedmineIssueFactory implements ServerExtension{
    private static List<IssuePriority> plist;
    private static List<Tracker> tlist;
    private Integer priorityId;
    private Integer trackerId;
    protected static RedmineManager redmineMgr;
    private I18n i18n;
    public RedmineIssueFactory (I18n i18n){
        this.i18n = i18n;
    }
  
    public Issue createRedmineIssue(org.sonar.api.issue.Issue sonarIssue,Settings settings) throws RedmineException {
        Issue redmineIssue = new Issue();
        redmineIssue.setPriorityId(mapRedminePriorityToId(
                          sonarSeverityToRedminePriority(RulePriority.valueOf(sonarIssue.severity()),settings),
                          redmineMgr)
                );
        redmineIssue.setSubject(generateIssueSummary(sonarIssue));
        redmineIssue.setTracker(new Tracker(
                          setTrackerId(settings.getString(RedmineConstants.REDMINE_ISSUE_TYPE),redmineMgr),
                          settings.getString(RedmineConstants.REDMINE_ISSUE_TYPE))
                );	


        redmineIssue.setDescription(generateIssueDescription(sonarIssue,settings));
        return redmineIssue;
  }
  protected String generateIssueSummary(org.sonar.api.issue.Issue sonarIssue) {
      StringBuilder summary = new StringBuilder("Sonar Issue #");
      summary.append(sonarIssue.message());
      return summary.toString();

}
  protected String generateIssueDescription(org.sonar.api.issue.Issue sonarIssue,Settings settings) throws RedmineException{
      StringBuilder description = new StringBuilder("Issue detail:\n");
      description.append(sonarIssue.key());
      description.append(" - ");
      description.append(sonarIssue.ruleKey());
      description.append("\n\nCheck it on Sonar: ");
      description.append(CoreProperties.SERVER_BASE_URL_DEFAULT_VALUE);
      description.append("/issue/show/");
      description.append(sonarIssue.key());
      
     
      return description.toString();

}
  protected String sonarSeverityToRedminePriority(RulePriority sonarSeverity,Settings settings) {
      final String redminePriority;
      switch(sonarSeverity){
         case INFO:
             redminePriority = settings.getString(RedmineConstants.REDMINE_INFO_PRIORITY_ID);
             break;   
         case MINOR:
             redminePriority = settings.getString(RedmineConstants.REDMINE_MINOR_PRIORITY_ID);
             break;   
         case MAJOR:
             redminePriority = settings.getString(RedmineConstants.REDMINE_MAJOR_PRIORITY_ID);
             break;
         case CRITICAL:
             redminePriority = settings.getString(RedmineConstants.REDMINE_CRITICAL_PRIORITY_ID);
             break;
         case BLOCKER:
             redminePriority = settings.getString(RedmineConstants.REDMINE_BLOCKER_PRIORITY_ID);
             break;
         default:
                 throw new SonarException("Unable to convert review severity to REDMINE priority: " + sonarSeverity);



          }
      return redminePriority;

}
  protected Integer mapRedminePriorityToId(String redminePriority,RedmineManager redmineMgr){
      plist = RedmineProperty.getPriorityValueFromRedmine(redmineMgr);
      for (IssuePriority s :plist) {
          if(redminePriority.equals(s.getName())){
              priorityId = s.getId();}
            }
      return priorityId;

  }
  protected Integer setTrackerId(String issueType,RedmineManager redmineMgr){
      tlist = RedmineProperty.getIssueTypeFromRedmine(redmineMgr);
      for (Tracker t : tlist) {
          if(issueType.equals(t.getName())){
              trackerId = t.getId();
           }
        }
      return trackerId;
  }
  
}