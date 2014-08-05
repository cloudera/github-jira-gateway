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

import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {

  appenders {
    def catalinaBase = System.properties.getProperty('catalina.base') ?: '.'
    def logDirectory = "${catalinaBase}/logs"

    appender new DailyRollingFileAppender(
            name: 'gatewayrolling',
            file: "${logDirectory}/gateway.log",
            layout: pattern(conversionPattern: '[%d{ISO8601}] [%t] %c{4}    %m%n'),
            datePattern: "'.'yyyy-MM-dd")

    rollingFile name: "stacktrace", maxFileSize: 1024,
            file: "${logDirectory}/stacktrace.log"
  }

  root {
    info 'gatewayrolling'
  }

  // Set level for all application artifacts
  info 'grails.app', 'com.cloudera.jiragateway', 'grails.app.taglib', 'grails.app.controller'

  warn 'org.codehaus.groovy.grails'

  // Set this to debug to watch the XML communications to and from Amazon
  error 'org.apache.http.wire'

  // Suppress most noise from libraries
  error 'grails.spring', 'net.sf.ehcache', 'org.springframework', 'org.hibernate',
          'org.apache.catalina', 'org.apache.commons', 'org.apache.coyote', 'org.apache.http.client.protocol',
          'org.apache.jasper', 'org.apache.tomcat', 'org.codehaus.groovy.grails'

  environments {
    def devConfig = {
      console name: 'stdout', layout: pattern(conversionPattern: '[%d{ISO8601}] %c{4}    %m%n')
      root {
        info 'stdout'
      }
    }
    development devConfig
  }
}

gatewayHome = System.getenv('GH_JIRA_GW_HOME') ?: System.getProperty('GH_JIRA_GW_HOME') ?:
        "${System.getProperty('user.home')}/.gh_jira_gw"

println "Using ${gatewayHome} as GH_JIRA_GW_HOME"

appConfigured = new File(gatewayHome, 'Config.groovy').exists()

// Locations to search for config files that get merged into the main config.
// Config files can either be Java properties files or ConfigSlurper scripts.
grails.config.locations = [
        "file:${gatewayHome}/Config.groovy",
        'classpath:sourceVersion.properties'
]

defaultCommentTemplate = '''\
Commit for this issue with hash ${commit.shortHash()} made to ${commit.branch} in ${commit.organization}/${commit.repository}
0Committer is ${commit.committer}, commit details [here|${commit.repoUrl}/commit/${commit.gitHash}]
'''
