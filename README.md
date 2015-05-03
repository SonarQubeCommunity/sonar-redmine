

SonarQube Redmine  Plugin
==========================
Download and Version information: http://update.sonarsource.org/plugins/redmine-confluence.html

## Build Status
[![Build Status](https://sonarplugins.ci.cloudbees.com/job/redmine/buildStatus/icon?job=check-manifest)](https://sonarplugins.ci.cloudbees.com/job/redmine)

## Description / Features
Description / Features
This plugin connects SonarQube to Redmine (http://www.redmine.org) issue and project management tool in various ways.
##### Widget "Redmine Issues"
SonarQube retrieves the number of open issues associated to a project from Redmine. It then reports on the total number of issues and distribution by priority.
##### Widget "Redmine Developers"
SonarQube retrieves the number of open issues associated to a project from Redmine. It then reports on the total number of issues and distribution by developers.
##### Link a SonarQube review to a Redmine Issue
This feature allows you to create a review (on a violation) that will generate a Redmine Issue on your configured Redmine Installation
When logged in, you should find the "Link to Redmine" action available on any violation:
You can enter any comment and after you press "Link to Redmine", a new review comment is added on the violation: you can see the link to the newly-created Redmine issue.

## Requirements
The plugins is only integrated with Redmine v2.2 or later.

## Installation
Install the plugin through the Update Center or download it into the SONARQUBE_HOME/extensions/plugins directory
Restart the SonarQube server

## Usage
Before you configure the plugin you need to get Redmine's API Access key
Log in to your Redmine installation with administration rights
Navigate to Administration > Settings > Authentication
Enable Rest Web Service API
Go to the "My Account" page ( /my/account ) and create a new API Access key on the right panel of your screen.
Copy the API Access key to use it in plugin configuration

## Plugin Configuration 
At Global level, go to Settings -> Redmine and set Redmine's URL and API Access key you copied from previous step 
At Project level, go to Configuration -> Redmine Configuration Page
Set the general settings for accessing Redmine : URL and the API Key you copied from previous step.
Set the project key and click on the <Test the settings; gets priorities and trackers> button
Pick up a default priority and default tracker for the redmine issues and save your settings
