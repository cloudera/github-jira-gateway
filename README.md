# GitHub/JIRA Gateway

## What is this thing here?

This is a service designed to be run somewhere and used as the
endpoint for GitHub Enterprise webhook push notifications to hit. It
takes the information from the push notifications, extracts relevant
JIRAs from the first line of the commit messages, and adds comments to
those JIRAs on your specified JIRA host.

## How do I set it up?

* Change grails-app/conf/DataSource.groovy to your desired setup.
* On the box you want it to run on, make sure Tomcat (or whatever
application server you're using) and Java are installed.
* If you're using an external database, install it, create the
appropriate database, and run "./grailsw prod dbm-update" from this repo
on the host.
* Build the war - "./grailsw prod war".
* Copy the war to Tomcat or whatever app server.
* Start Tomcat or whatever app server.
* Update the gateway_config table to have the right values for
jira_username and jira_password - UI for this incoming.
* You can watch the logs in /var/log/tomcat6/catalina.out, or wherever
your app server's logs are.

## What about on the GitHub side?

For each repository you want to notify this service, you'll need to
set up a webhook.

### GitHub Enterprise 11.10.340 and newer

If you're running a newer version of GitHub Enterprise, you should be
able to just add a new webhook through the web UI - specify the URL
for the service (i.e.,
http://172.21.2.212:8080/github-jira-gateway/incomingCommit/save),
"application/json" as the content type, and "Just the push event" -
you shouldn't need a secret.

### GitHub Enterprise versinos older than 11.10.340

For older versions of GitHub Enterprise (i.e., if you don't get a form
that sounds like the above), you'll need to add the hook through the
GitHub Enterprise REST API.

To do that, create a JSON file like the below - change the host and
port as appropriate:

    {
      "name": "web",
      "active": true,
      "events": [
        "push"
      ],
      "config": {
        "url": "http://172.21.2.212:8080/github-jira-gateway/incomingCommit/save",
        "content_type": "json"
      }
    }

Then get your GitHub Enterprise authentication token (from
http://github.somedomain.com/settings/applications in the Personal
Access Tokens section - create one if there isn't one there yet).

Take that token, and for each repository you want, run

     curl -H "Authorization: token TOKEN_HERE" -H "Content-Type: application/json" -X POST --data-binary  @hook.json http://github.somedomain.com/api/v3/repos/ORGANIZATION/REPOSITORY/hooks

This will add the hook to the repository, and all pushes to the
repository from now on will trigger notifications being sent to the
serivce.

## What can I customize?

You can change the jira_projects and comment_template fields in the
gateway_config table.

jira_projects is just a comma-separated list of projects in JIRA.

comment_template is the template used to generate the comment that'll
be added to the relevant JIRA issue. Available fields from the commit
object are:

* gitHash - the full git hash.
* shortHash() - helper method that will give you just the first 8
characters of the git hash.
* branch - The branch this commit was pushed to.
* message - The full commit message.
* author - The git author of the commit.
* committer - The git committer of the commit - this would be whoever
cherry picked the commit, even if they didn't author the original commit.
* timestamp - The time the commit was made - not when it was pushed,
but when it was made locally.
* repository - The git repository name this is in - i.e., "github-jira-gateway".
* organization - The GitHub Enterprise organization the repo is in -
i.e., "abayer"
* repoUrl - A link to the commit diff and comment on GitHub Enterprise.
* issues() - A comma-separated list of all JIRAs we saw on this commit.


The default template is as follows:

    Commit for this issue with hash ${commit.shortHash()} made to ${commit.branch} in ${commit.organization}/${commit.repository}
    Committer is ${commit.committer}, commit details [here|${commit.repoUrl}/commit/${commit.gitHash}]

## Any gotchas?

A given git hash will only be processed once - if it shows up as part
of pushing another branch in the future, it will be ignored.
