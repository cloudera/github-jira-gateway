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
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(IncomingCommitController)
@Mock(IncomingCommit)
class IncomingCommitControllerSpec extends Specification {
  def configService = new ConfigService(grailsApplication: Mocks.grailsApplication())

  def setup() {
    controller.configService = configService
  }

  def 'should save single commit'() {
    request.json = '{"ref":"refs/heads/master","after":"4aaec8568ddbf968018cb00486220a226e46b3ad","before":"fd394d7824d189c834822a4bb756af40e8ce4950","created":false,"deleted":false,"forced":true,"compare":"http://github.mtv.cloudera.com/Kitchen/cloudstack-reporting/compare/fd394d7824d1...4aaec8568ddb","commits":[{"id":"4aaec8568ddbf968018cb00486220a226e46b3ad","distinct":true,"message":"[#KITCHEN-5260] Testing JIRA - ignore\\n\\nfoobarbaz","timestamp":"2014-08-01T15:47:34-07:00","url":"http://github.mtv.cloudera.com/Kitchen/cloudstack-reporting/commit/4aaec8568ddbf968018cb00486220a226e46b3ad","author":{"name":"Andrew Bayer","email":"andrew.bayer@gmail.com"},"committer":{"name":"Andrew Bayer","email":"andrew.bayer@gmail.com"},"added":[],"removed":[],"modified":[]}],"head_commit":{"id":"4aaec8568ddbf968018cb00486220a226e46b3ad","distinct":true,"message":"[#KITCHEN-5260] Testing JIRA - ignore\\n\\nfoobarbaz","timestamp":"2014-08-01T15:47:34-07:00","url":"http://github.mtv.cloudera.com/Kitchen/cloudstack-reporting/commit/4aaec8568ddbf968018cb00486220a226e46b3ad","author":{"name":"Andrew Bayer","email":"andrew.bayer@gmail.com"},"committer":{"name":"Andrew Bayer","email":"andrew.bayer@gmail.com"},"added":[],"removed":[],"modified":[]},"repository":{"id":1255,"name":"cloudstack-reporting","url":"http://github.mtv.cloudera.com/Kitchen/cloudstack-reporting","description":"","watchers":4,"stargazers":4,"forks":2,"fork":true,"size":1702,"owner":{"name":"Kitchen","email":null},"private":false,"open_issues":2,"has_issues":false,"has_downloads":true,"has_wiki":true,"language":"Groovy","created_at":1352528755,"pushed_at":1406933269,"master_branch":"master","organization":"Kitchen"},"pusher":{"name":"andrew","email":"andrew@cloudera.com"}}'

    when:
    controller.save()

    then:
    JSON.parse("{message:\"OK\"}") == response.json
    1 == IncomingCommit.count()
    1 == IncomingCommit.get(1).jiras.size()
    "KITCHEN-5260" == IncomingCommit.get(1).jiras.first().issueId
  }
}
