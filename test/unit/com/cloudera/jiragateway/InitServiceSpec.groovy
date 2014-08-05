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

import grails.test.MockUtils
import grails.test.mixin.TestFor
import org.springframework.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(InitService)
class InitServiceSpec extends Specification {
  static final String GATEWAY_HOME = 'gatewaytmp'

  @AutoCleanup('delete')
  File configFile = new File(GATEWAY_HOME, 'Config.groovy') // Only used for cleanup
  @AutoCleanup('delete')
  File gatewayHome = new File(GATEWAY_HOME)

  def applicationContext = Mock(ApplicationContext)
  def configService = Mock(ConfigService)
  ConfigObject config = new ConfigObject()
  def initService

  void setup() {
    MockUtils.mockLogging(InitService)
    initService = new InitService(applicationContext: applicationContext, configService: configService,
            grailsApplication: [config: config])
  }

  def 'should create config file'() {
    configService.gatewayHome >> GATEWAY_HOME
    ConfigObject newConfig = new ConfigObject()
    newConfig['test'] = 'testVal'

    when:
    initService.writeConfig(newConfig)

    then:
    ConfigObject savedConfig = new ConfigSlurper().parse(new File("${GATEWAY_HOME}/Config.groovy").toURI().toURL())
    newConfig == savedConfig
    config.appConfigured == true
    config.test == 'testVal'
  }

}
