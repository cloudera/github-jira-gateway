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

import org.apache.commons.logging.LogFactory

class InitController {
  private static final log = LogFactory.getLog(this)

  def initService
  def configService

  static allowedMethods = [save: 'POST']

  def beforeInterceptor = {
    if (configService.appConfigured) {
      redirect(controller: 'home')
      return false
    }
  }

  def index() {
    [gatewayHome: configService.gatewayHome, defaultCommentTemplate: configService.defaultCommentTemplate]
  }

  /**
   * Creates the Config.groovy file from the supplied parameters and redirects to the home page if successful
   */
  def save(InitializeCommand cmd) {
    if (cmd.hasErrors()) {
      render(view: 'index', model: [cmd: cmd])
      return
    }
    try {
      initService.writeConfig(cmd.toConfigObject())
    } catch (Exception ioe) {
      flash.message = ioe.message
      redirect(action: 'index')
      return
    }
    flash.message = "Created GH/JIRA Gateway configuration file at ${configService.gatewayHome}/Config.groovy."
    redirect(controller: 'home')
  }
}

class InitializeCommand {
  String jiraRootUrl
  String jiraUsername
  String jiraPassword
  String jiraProjects
  String commentTemplate

  static constraints = {
    jiraProjects()
    jiraRootUrl()
    jiraUsername()
    jiraPassword(password: true)
    commentTemplate()
  }

  ConfigObject toConfigObject() {
    ConfigObject rootConfig = new ConfigObject()
    ConfigObject grailsConfig = new ConfigObject()
    rootConfig['grails'] = grailsConfig
    grailsConfig['jiraRootUrl'] = jiraRootUrl
    grailsConfig['jiraUsername'] = jiraUsername
    grailsConfig['jiraPassword'] = jiraPassword
    grailsConfig['commentTemplate'] = commentTemplate
    grailsConfig['jiraProjects'] = jiraProjects
    rootConfig
  }
}
