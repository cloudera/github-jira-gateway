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

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class InitService implements ApplicationContextAware {

  static transactional = false

  ApplicationContext applicationContext

  def configService
  def grailsApplication // modifying the config object directly here

  /**
   * Creates the GH/Jira Gateway Config.groovy file and updates the in memory configuration to reflect the configured state
   *
   * @param configObject The configuration to persist
   */
  void writeConfig(ConfigObject configObject) throws IOException {
    File gatewayHomeDir = new File(configService.gatewayHome)
    gatewayHomeDir.mkdirs()
    if (!gatewayHomeDir.exists()) {
      throw new IOException("Unable to create directory ${configService.gatewayHome}")
    }

    File configFile = new File(configService.gatewayHome, 'Config.groovy')
    boolean fileCreated = configFile.createNewFile()
    if (!fileCreated) {
      throw new IOException("Unable to create Config.groovy file in directory ${configService.gatewayHome}")
    }
    configFile.withWriter { writer ->
      configObject.writeTo(writer)
    }
    grailsApplication.config.appConfigured = true
    grailsApplication.config.merge(configObject)

  }

}
