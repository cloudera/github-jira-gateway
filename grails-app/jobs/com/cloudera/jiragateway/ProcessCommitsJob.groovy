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
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace

import groovy.text.SimpleTemplateEngine
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import org.apache.commons.logging.LogFactory
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.HttpResponse
import org.apache.http.impl.client.DefaultRedirectStrategy
import org.apache.http.protocol.HttpContext

class ProcessCommitsJob {
  private static final log = LogFactory.getLog(this)

  def concurrent = false

  static triggers = {
    simple name: 'processTrigger', startDelay:15000, repeatInterval:60000, repeatCount:-1
  }

  def group  = "processCommits"

  def execute() {
    // execute job
    log.warn("Starting commit processing...")
    def commits = IncomingCommit.findAllByProcessed(false)

    GatewayConfig config = GatewayConfig.get(1)
    if (commits.size() > 0) {
      def http = getHttp()
      commits.each { commit ->
        log.warn("Processing JIRAs for commit ${commit.shortHash()}")
        commit.jiras.each { j ->
          if (!j.processed && j.attempts < 3) {
            log.warn("Commenting on JIRA ${j.issueId}...")
            try {
              def jsonBody = ["body": commentBody(commit)]
              http.request(POST, JSON) { req ->
                uri.path = "/rest/api/latest/issue/${j.issueId}/comment"
                body = jsonBody
                response.success = { resp ->
                  j.processed = true
                  j.save()
                  log.warn("Successfully commented on ${j.issueId}")
                }
                response.failure = { HttpResponseDecorator resp, json ->
                  log.error("Response error: ${resp.allHeaders} - ${json}")
                  j.attempts += 1
                  j.save()
                }
              }
            } catch (Exception e) {
              log.error("Error when updating jira ${j.issueId}: ${getFullStackTrace(e)}")
              j.attempts += 1
              j.save()
            }
          }
        }
        if (commit.jiras.every { it.processed }) {
          log.warn("Finished all JIRAs for ${commit.shortHash()}")
          commit.processed = true
          commit.save()
        }
      }
    } else {
      log.warn("No new commits to process")
    }
  }
  private commentBody(IncomingCommit commit) {
    GatewayConfig config = GatewayConfig.get(1)
    return new SimpleTemplateEngine().createTemplate(config.commentTemplate).make(commit: commit).toString()
  }

  private getHttp() {
    GatewayConfig config = GatewayConfig.get(1)
    def url = "${config.jiraHost}/"

    def http = new HTTPBuilder(url)
    http.client.addRequestInterceptor(new HttpRequestInterceptor() {
      void process(HttpRequest httpRequest, HttpContext httpContext) {
        httpRequest.addHeader('Authorization', 'Basic ' + "${config.jiraUsername}:${config.jiraPassword}".toString().bytes.encodeBase64().toString())
      }
    })
    http.client.setRedirectStrategy(new DefaultRedirectStrategy() {
      @Override
      boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
        def redirected = super.isRedirected(request, response, context)
        return redirected || response.getStatusLine().getStatusCode() == 302
      }
    })
    return http

  }
}
