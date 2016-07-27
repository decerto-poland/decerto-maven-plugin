# Decerto Maven Plugin
[![Build Status](https://travis-ci.org/decerto-poland/decerto-maven-plugin.svg?branch=master)](https://travis-ci.org/decerto-poland/decerto-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/github/decerto-poland/decerto-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/decerto-poland/decerto-maven-plugin?branch=master)

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
