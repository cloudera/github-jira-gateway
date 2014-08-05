/*
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.jiragateway

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

class ConfigService {

  static transactional = false

  def grailsApplication

  /**
   * @return true if minimum configuration has been provided, false otherwise
   */
  boolean isAppConfigured() {
    grailsApplication.config.appConfigured
  }

  /**
   * @return The configured JIRA username
   */
  String getJiraUsername() {
    grailsApplication.config.jiraUsername
  }

  /**
   * @return The directory that will/does contain GH/Jira Gateway's Config.groovy
   */
  String getGatewayHome() {
    grailsApplication.config.gatewayHome
  }

  /**
   * @return The configured JIRA password
   */
  String getJiraPassword() {
    grailsApplication.config.jiraPassword
  }

  /**
   * @return The configured JIRA root URL
   */
  String getJiraRootUrl() {
    grailsApplication.config.jiraRootUrl
  }

  /**
   * @return The configured JIRA projects
   */
  Collection<String> getJiraProjects() {
    getJiraProjectsAsString().split(",").collect { String p -> p.trim() }
  }

  /**
   * @return The configured JIRA projects as a single string
   */
  String getJiraProjectsAsString() {
    grailsApplication.config.jiraProjects
  }

  /**
   * @return The configured JIRA comment template as a string
   */
  String getCommentTemplateAsString() {
    grailsApplication.config.commentTemplate
  }

  /**
   * @return The configured JIRA comment template as a {@link Template}
   */
  Template getCommentTemplate() {
    return new SimpleTemplateEngine().createTemplate(getCommentTemplateAsString())
  }

  /**
   * @return The default JIRA comment template as a string
   */
  String getDefaultCommentTemplate() {
    grailsApplication.config.defaultCommentTemplate
  }
}
