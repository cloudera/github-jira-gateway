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

databaseChangeLog = {

	changeSet(author: "abayer (generated)", id: "1407181402028-1") {
		createTable(tableName: "gateway_config") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "gateway_confiPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "comment_template", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "jira_host", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "jira_password", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "jira_projects", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "jira_username", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "abayer (generated)", id: "1407181402028-2") {
		createTable(tableName: "incoming_commit") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "incoming_commPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "author", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "branch", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "committer", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "git_hash", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(255)")

			column(name: "organization", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "processed", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "repo_url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "repository", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "abayer (generated)", id: "1407181402028-3") {
		createTable(tableName: "incoming_commit_jira") {
			column(name: "incoming_commit_jiras_id", type: "bigint")

			column(name: "jira_id", type: "bigint")
		}
	}

	changeSet(author: "abayer (generated)", id: "1407181402028-4") {
		createTable(tableName: "jira") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "jiraPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "attempts", type: "integer") {
				constraints(nullable: "false")
			}

			column(name: "issue_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "processed", type: "bit") {
				constraints(nullable: "false")
			}
		}
	}

  changeSet(author: "abayer (generated)", id: "1407181402028-5") {
    createIndex(indexName: "FK7DCCCF1DB5C0D69B", tableName: "incoming_commit_jira") {
      column(name: "incoming_commit_jiras_id")
    }
  }

  changeSet(author: "abayer (generated)", id: "1407181402028-6") {
    createIndex(indexName: "FK7DCCCF1DC58D9014", tableName: "incoming_commit_jira") {
      column(name: "jira_id")
    }
  }

	changeSet(author: "abayer (generated)", id: "1407181402028-7") {
		addForeignKeyConstraint(baseColumnNames: "incoming_commit_jiras_id", baseTableName: "incoming_commit_jira", constraintName: "FK7DCCCF1DB5C0D69B", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "incoming_commit", referencesUniqueColumn: "false")
	}

	changeSet(author: "abayer (generated)", id: "1407181402028-8") {
		addForeignKeyConstraint(baseColumnNames: "jira_id", baseTableName: "incoming_commit_jira", constraintName: "FK7DCCCF1DC58D9014", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "jira", referencesUniqueColumn: "false")
	}

}
