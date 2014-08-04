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

import grails.converters.JSON
import org.apache.commons.logging.LogFactory

class IncomingCommitController {
  private static final log = LogFactory.getLog(this)

  static allowedMethods = [save: ["POST", "GET"], update: ["POST", "GET"], delete: ["POST", "GET"], destroy: ["POST", "GET"]]

  def index() { }

  def save() {
    params.putAll(request.JSON)
    def branch = params.ref.replaceAll("refs/heads/", "")
    params.commits.each { c ->
      if (IncomingCommit.findByGitHash(c.id) == null) {
        def jiras = findJiras(c.message)
        if (jiras.size() > 0) {
          IncomingCommit commit = new IncomingCommit(branch: branch)
          commit.gitHash = c.id
          commit.author = c.author.email ?: "unspecified"
          commit.committer = c.committer.email ?: "unspecified"
          commit.timestamp = Date.parse("yyyy-MM-dd'T'HH:mm:ssZ", c.timestamp.replaceAll(/(\-\d\d):(\d\d)/, '$1$2'))
          commit.message = c.message
          commit.organization = params.repository.organization
          commit.repository = params.repository.name
          commit.repoUrl = params.repository.url
          commit.jiras = jiras.collect { new Jira(issueId: it, processed: false) }

          commit.processed = false

          if (commit.validate()) {
            commit.save()
            log.warn("Adding commit ${commit.shortHash()} for JIRAs ${commit.jiras.collect { it.issueId }.join(', ') } by ${commit.committer}")
            response.status = 204
            render([message: "OK"] as JSON)
          } else {
            commit.errors.allErrors.each {
              log.warn("commit ${commit.id} error: ${it}")
            }
            response.status = 500
            render([message: "ERROR", errors: commit.errors.allErrors] as JSON)
          }
        } else {
          log.warn("No JIRAs in message for ${c.id} so ignoring")
          response.status = 200
          render([message: "NO JIRAS"] as JSON)
        }
      } else {
        log.warn("Already saw commit ${c.id} so ignoring")
        response.status = 200
        render([message: "ALREADY SEEN"] as JSON)
      }
    }
  }

  private findJiras(String message) {
    def jiraProjects = GatewayConfig.get(1).jiraProjects.replaceAll(",", "|")

    def p = ~/\b((?:${jiraProjects})-\d+)/

    def m = message =~ p

    if (m.size() > 0) {
      return m.collect { it[0] }
    } else {
      return []
    }
  }

}
