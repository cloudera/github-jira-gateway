databaseChangeLog = {

	changeSet(author: "abayer (generated)", id: "1407276827264-1") {
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

			column(name: "date_created", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "git_hash", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "last_updated", type: "timestamp") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "varchar(255)")

			column(name: "organization", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "processed", type: "boolean") {
				constraints(nullable: "false")
			}

			column(name: "repo_url", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "repository", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "timestamp") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "abayer (generated)", id: "1407276827264-2") {
		createTable(tableName: "incoming_commit_jira") {
			column(name: "incoming_commit_jiras_id", type: "bigint")

			column(name: "jira_id", type: "bigint")
		}
	}

	changeSet(author: "abayer (generated)", id: "1407276827264-3") {
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

			column(name: "processed", type: "boolean") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "abayer (generated)", id: "1407276827264-4") {
		addForeignKeyConstraint(baseColumnNames: "incoming_commit_jiras_id", baseTableName: "incoming_commit_jira", constraintName: "FK_6sa1q3dyf7avo1ikvxs9ac686", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "incoming_commit", referencesUniqueColumn: "false")
	}

	changeSet(author: "abayer (generated)", id: "1407276827264-5") {
		addForeignKeyConstraint(baseColumnNames: "jira_id", baseTableName: "incoming_commit_jira", constraintName: "FK_411j0uthpixmwx543xqd87qlu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "jira", referencesUniqueColumn: "false")
	}
}
