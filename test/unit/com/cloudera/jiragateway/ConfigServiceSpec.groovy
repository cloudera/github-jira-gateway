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

import com.cloudera.jiragateway.mock.Mocks
import grails.test.mixin.TestFor
import groovy.text.SimpleTemplateEngine
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ConfigService)
class ConfigServiceSpec extends Specification {
  ConfigService configService = new ConfigService(grailsApplication: Mocks.grailsApplication())

  def 'should return JIRA root URL'() {
    expect:
    "http://foo.foo" == configService.jiraRootUrl
  }

  def 'should return JIRA Username'() {
    expect:
    "username" == configService.jiraUsername
  }

  def 'should return JIRA Password'() {
    expect:
    "password" == configService.jiraPassword
  }

  def 'should return JIRA Projects as String'() {
    expect:
    "PROJ1,PROJ2, KITCHEN" == configService.jiraProjectsAsString
  }

  def 'should return JIRA Projects as Collection'() {
    expect:
    ["PROJ1", "PROJ2", "KITCHEN"] == configService.jiraProjects
  }

  def 'should return comment template as string'() {
    "nothing" == configService.commentTemplateAsString
  }

  def 'should return comment template as Template'() {
    new SimpleTemplateEngine().createTemplate("nothing").equals(configService.commentTemplate)
  }
}
