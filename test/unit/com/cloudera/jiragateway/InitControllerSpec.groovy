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

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(InitController)
class InitControllerSpec extends Specification {

  def configService = Mock(ConfigService)
  def initService = Mock(InitService)

  def setup() {
    TestUtils.setUpMockRequest()

    mockForConstraintsTests(InitializeCommand)
    controller.initService = initService
    controller.configService = configService
  }

  def 'should create config file'() {
    InitializeCommand command = new InitializeCommand(jiraProjects: "PROJ1", jiraRootUrl: "http://foo.foo",
            jiraPassword: "password", jiraUsername: "username", commentTemplate: "nothing")
    configService.getGatewayHome() >> 'gatewayHomeDir'
    configService.isAppConfigured() >> false
    request.method = "POST"

    when:
    controller.save(command)

    then:
    '/home' == response.redirectUrl
    'Created GH/JIRA Gateway configuration file at gatewayHomeDir/Config.groovy.' == controller.flash.message
    1 * initService.writeConfig(_)
  }

  def 'should redirct with flash message for IOException'() {
    InitializeCommand command = new InitializeCommand(jiraProjects: "PROJ1", jiraRootUrl: "http://foo.foo",
            jiraPassword: "password", jiraUsername: "username", commentTemplate: "nothing")
    request.method = "POST"
    initService.writeConfig(_) >> { throw new IOException('This error') }

    when:
    controller.save(command)

    then:
    'This error' == controller.flash.message
    '/init/index' == response.redirectUrl
  }

  def 'should return error for invalid request'() {
    InitializeCommand command = new InitializeCommand()
    command.validate()
    request.method = "POST"

    when:
    controller.save(command)

    then:
    command.hasErrors()
    '/init/index' == view
  }

}
