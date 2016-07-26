package pl.decerto.plugin.service.impl;

import static pl.decerto.plugin.model.LiquibaseDefaultProperties.DEFAULT_SNAPSHOT_DIR_SIZE;
import static pl.decerto.plugin.model.LiquibaseDefaultProperties.TEMPLATE_DIR;
import static pl.decerto.plugin.model.LiquibaseDefaultProperties.VERSION_CHANGELOG_FILE;
import static pl.decerto.plugin.model.LoggerMessages.CHANGELOG_INCLUDE_MSG;
import static pl.decerto.plugin.model.LoggerMessages.COMMIT_MSG;
import static pl.decerto.plugin.model.LoggerMessages.MOVE_DIR_MSG;
import static pl.decerto.plugin.model.LoggerMessages.NO_SNAPSHOTS_MSG;
import static pl.decerto.plugin.model.LoggerMessages.PLACEHOLDER_PATTERN;
import static pl.decerto.plugin.model.LoggerMessages.SNAPSHOT_CHANGELOG_INCLUDE;
import static pl.decerto.plugin.model.LoggerMessages.TEMPLATE_MSG;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import pl.decerto.plugin.git.GitService;
import pl.decerto.plugin.git.impl.GitServiceImpl;
import pl.decerto.plugin.model.MavenProperties;
import pl.decerto.plugin.service.LiquibaseSnapshotService;

public class LiquibaseSnapshotServiceImpl implements LiquibaseSnapshotService {

	private final Log logger;

	private final MavenProperties mavenProperties;

	private final GitService gitService;

	public LiquibaseSnapshotServiceImpl(MavenProperties mavenProperties) {
		this.mavenProperties = mavenProperties;
		this.gitService = new GitServiceImpl(mavenProperties.getProjectVersion());
		this.logger = mavenProperties.getLogger();
	}

	@Override
	public void moveSnapshots() {
		if(thereAreChanges()) {
			processSnapshotFiles();
			commitChanges();
		} else {
			logger.info(NO_SNAPSHOTS_MSG);
		}
	}

	private boolean thereAreChanges() {
		File snapshotDir = getSnapshotDir();
		return snapshotDir.listFiles() != null && snapshotDir.listFiles().length > DEFAULT_SNAPSHOT_DIR_SIZE;
	}

	private void processSnapshotFiles() {
		try {
			moveSnapshotFilesToProjectVersionDir();
			createEmptyChangeLogFile();
			modifyChangelogFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void modifyChangelogFiles() throws IOException {
		for(String changelogFile : mavenProperties.getChangelogFiles()) {
			modifyChangelogFile(changelogFile);
		}
	}

	private void modifyChangelogFile(String changelogFilename) throws IOException {
		File changelogFile = getChangelogFile(changelogFilename);
		replaceContentInFile(changelogFile);
	}

	private void replaceContentInFile(File changelogFile) throws IOException {
		logger.info(MessageFormat.format(CHANGELOG_INCLUDE_MSG, changelogFile.getName()));
		String content = FileUtils.readFileToString(changelogFile);
		String modifiedContent = content.replace(PLACEHOLDER_PATTERN, getFormattedSnapshotChangelogInclude());
		FileUtils.forceDelete(changelogFile);
		FileUtils.writeStringToFile(changelogFile, modifiedContent);
	}

	private void moveSnapshotFilesToProjectVersionDir() throws IOException {
		logger.info(MessageFormat.format(MOVE_DIR_MSG, mavenProperties.getProjectVersion()));
		File snapshotDir = getSnapshotDir();
		File projectVersionDir = getProjectVersionDir();
		FileUtils.moveDirectory(snapshotDir, projectVersionDir);
	}

	private void createEmptyChangeLogFile() throws IOException {
		logger.info(TEMPLATE_MSG);
		File snapshotDir = getSnapshotDir();
		File templateChangeLogFile = getChangelogTemplateFile();
		copyFileToDestinationFromSource(snapshotDir, templateChangeLogFile);
	}

	private void copyFileToDestinationFromSource(File snapshotDir, File templateChangeLogFile) throws IOException {
		if(!snapshotDir.exists()) {
			snapshotDir.mkdir();
		}
		FileUtils.copyFileToDirectory(templateChangeLogFile, snapshotDir);
	}

	private void commitChanges() {
		logger.info(COMMIT_MSG);
		gitService.commitDirectory(getLiquibaseDir());
	}

	private File getProjectVersionDir() {
		return new File(mavenProperties.getProjectBaseDir() + mavenProperties.getLiquibaseDir() + mavenProperties.getProjectVersion());
	}

	private File getSnapshotDir() {
		return new File(mavenProperties.getProjectBaseDir() + mavenProperties.getLiquibaseSnapshotDir());
	}

	private File getChangelogTemplateFile() {
		return new File(mavenProperties.getProjectBaseDir() + mavenProperties.getLiquibaseDir() + TEMPLATE_DIR + VERSION_CHANGELOG_FILE);
	}

	private File getLiquibaseDir() {
		return new File(mavenProperties.getProjectBaseDir() + mavenProperties.getLiquibaseDir());
	}

	private File getChangelogFile(String changelogFile) {
		return new File(mavenProperties.getProjectBaseDir() + mavenProperties.getLiquibaseDir() + changelogFile);
	}

	private String getFormattedSnapshotChangelogInclude() {
		return MessageFormat.format(SNAPSHOT_CHANGELOG_INCLUDE, mavenProperties.getProjectVersion(), mavenProperties.getLineSeparator());
	}
}
