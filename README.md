# Decerto Maven Plugin
[![Build Status](https://travis-ci.org/decerto-poland/decerto-maven-plugin.svg?branch=master)](https://travis-ci.org/decerto-poland/decerto-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/pl.decerto/decerto-liquibase-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)

This repository contains a set of useful Maven plugins.

- <b>Decerto Liquibase Maven Plugin</b> - This plugin is made for enhancing Liquibase projects automation

## Decerto Liquibase Maven Plugin
This plugin provides following goals:

- <b>moveSnapshots</b> (phase <i>none</i>) - enables moving temporary changes from (in default) <i>/resources/liquibase/snapshot</i> directory of the project. Useful during releasing a new version - it creates new folder in <i>/resources/liquibase/</i> with a project's current version name.
After succesful file processing, it creates proper git commit, which is provided for releasing automation through CI like Jenkins.

<b>Configuration properties:</b>

- liquibaseDir (default value -> <i>/src/main/resources/liquibase/</i>)
- liquibaseSnapshotDir (default value -> <i>/src/main/resources/liquibase/snapshot/</i>)
- projectVersion (default value -> project version from maven properties)
- projectBaseDir (default value -> project base dir from maven properties)
- artifactName  (default value -> <i>finalName</i> from maven properties)
- lineSeparator
- changelogFiles (list of changelog file names located in <i>/resources/liquibase/</i> - each files is extented by new snapshots folder inclusion)

<b> Usage: </b>
```
<plugins>
	<plugin>
		<artifactId>decerto-liquibase-maven-plugin</artifactId>
		<groupId>pl.decerto</groupId>
		<version>1.4.6</version>
		<configuration>
			<changelogFiles>
				<changelogFile>changelog-master.xml</changelogFile>
				<changelogFile>changelog-test.xml</changelogFile>
			</changelogFiles>
		</configuration>
		<executions>
			<execution>
				<goals>
					<goal>moveSnapshots</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
</plugins>
```
