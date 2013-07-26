package org.sonar.plugins.redmine;

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


import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.IssuePriority;
import com.taskadapter.redmineapi.bean.Tracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sonar.api.ServerExtension;
import org.sonar.plugins.redmine.reviews.RedmineLinkFunction;


/**
 *
 * @author gurpinars
 */
public class RedmineProperty implements ServerExtension{
    private static ArrayList<String> strList;
    private static List<IssuePriority> plist;
    private static List<Tracker> tlist;
   
    
    
  
   public static List<IssuePriority> getPriorityValueFromRedmine(RedmineManager redmineMgr){
      
       try {
           Properties configuration = getConfiguration();
           String host = configuration.getProperty("property.sonar.redmine.host");
           String apiKey = configuration.getProperty("property.sonar.redmine.api-access-key");
           
           redmineMgr = new RedmineManager(host,apiKey);
           try {
               plist = redmineMgr.getIssuePriorities();
               } catch (RedmineException e) {
                   e.printStackTrace();
              }

          } catch (IOException ex) {
            Logger.getLogger(RedminePlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
   return plist;
  }
   public static List<Tracker> getIssueTypeFromRedmine(RedmineManager redmineMgr){
       try {
           Properties configuration = getConfiguration();
           String host = configuration.getProperty("property.sonar.redmine.host");
           String apiKey = configuration.getProperty("property.sonar.redmine.api-access-key");
         
      
           
           redmineMgr = new RedmineManager(host,apiKey);
           try {
               tlist = redmineMgr.getTrackers();
	} catch (RedmineException e) {
            e.printStackTrace();
	}
    
       }catch (IOException ex) {
            Logger.getLogger(RedminePlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
       return tlist;
  }
    private static Properties getConfiguration() throws IOException{
       Properties properties = new Properties();
       properties.load(RedmineProperty.class.getClassLoader().getResourceAsStream("org/sonar/l10n/redmine.properties"));       
       return properties;

  }
    protected static ArrayList<String> convertObjListToStrList(List list){
        strList = new ArrayList<String>();
        int index = 0;
        if(list.get(index) instanceof IssuePriority){
            for (Iterator it = list.iterator(); it.hasNext();) {
                IssuePriority s = (IssuePriority) it.next();
                strList.add(index, s.getName());
                index++;
            }
        }
        else if(list.get(index) instanceof Tracker){
            for (Iterator it = list.iterator(); it.hasNext();) {
                Tracker s = (Tracker) it.next();
                strList.add(index, s.getName());
                index++;
            }
        
        
    
    }
        return strList;
    
}

}
