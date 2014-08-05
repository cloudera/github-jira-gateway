%{--
  - Licensed to Cloudera, Inc. under one
  - or more contributor license agreements.  See the NOTICE file
  - distributed with this work for additional information
  - regarding copyright ownership.  Cloudera, Inc. licenses this file
  - to you under the Apache License, Version 2.0 (the
  - "License"); you may not use this file except in compliance
  - with the License.  You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Initialize GitHub/JIRA Gateway</title>
    <meta name="layout" content="main"/>
    <meta name="hideNav" content="true"/>
</head>
<body>
<div class="body">
    <h1>Welcome to a GitHub/JIRA Gateway!!</h1>
    <h1>Enter them below to create a GitHub/JIRA Gateway configuration file at ${gatewayHome}/Config.groovy.</h1>
    <h1>For more advanced configuration, please consult the the documentation.</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${cmd}">
        <div class="errors">
            <g:renderErrors bean="${cmd}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form method="post" class="validate">
        <div class="dialog">
            <table>
                <tbody>
                <tr class="prop">
                    <td class="name">
                        <label for="jiraRootUrl">JIRA Root URL:</label>
                    </td>
                    <td class="value"><input type="text" size='25' maxlength='20' id="jiraRootUrl" name="jiraRootUrl" value="${params.jiraRootUrl}" class="required"/></td>
                </tr>
                <tr class="prop">
                    <td class="name">
                        <label for="jiraUsername">JIRA Username:</label>
                    </td>
                    <td class="value"><input type="text" size='25' maxlength='20' id="jiraUsername" name="jiraUsername" value="${params.jiraUsername}" class="required"/></td>
                </tr>
                <tr class="prop">
                    <td class="name">
                        <label for="jiraPassword">JIRA Password:</label>
                    </td>
                    <td class="value"><input type="password" size='25' maxlength='40' id="jiraPassword" name="jiraPassword" value="${params.jiraPassword}" class="required"/></td>
                </tr>
                <tr class="prop">
                    <td class="name">
                        <label for="jiraProjects">JIRA Projects (comma separated):</label>
                    </td>
                    <td class="value"><input type="text" size='50' maxlength='100' id="jiraProjects" name="jiraProjects" value="${params.jiraProject}" class="required"/></td>
                </tr>
                <tr class="prop">
                    <td class="name">
                        <label for="commentTemplate">JIRA Comment Template:</label>
                    </td>
                    <td class="value"><textarea rows="5" cols="80" id="commentTemplate" name="commentTemplate"
                                                value="${params.commentTemplate ?: defaultCommentTemplate}" class="required"></textarea></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="buttons">
            <g:buttonSubmit class="save" value="save">Save</g:buttonSubmit>
        </div>
    </g:form>
</div>

</body>
</html>