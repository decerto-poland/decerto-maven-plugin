package pl.decerto.plugin.git.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import pl.decerto.plugin.git.GitService;

public class GitServiceImpl implements GitService {

	private static final String COMMIT_MESSAGE = "Adding liquibase to version {0}";

	private final String projectVersion;

	private Repository repo;

	public GitServiceImpl(String projectVersion) {
		this.projectVersion = projectVersion;
		openRepository();
	}

	@Override
	public void commitDirectory(File commitDir) {
		try {
			addFilesFrom(commitDir);
			commitWithMessage(formatCommitMsg());
		} catch (GitAPIException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void addFilesFrom(File commitDir) throws GitAPIException {
		AddCommand git = new Git(repo).add();
		git.addFilepattern(getPath(commitDir))
			.call();
	}

	private void commitWithMessage(String commitMsg) throws GitAPIException {
		CommitCommand git = new Git(repo).commit();
		git.setMessage(commitMsg)
			.setInsertChangeId(true)
			.call();
	}

	private void openRepository() {
		try {
			repo = new FileRepositoryBuilder().findGitDir().build();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private String formatCommitMsg() {
		return MessageFormat.format(COMMIT_MESSAGE, projectVersion);
	}

	private String getPath(File file) {
		return getRelativePath(file).replace('\\', '/');
	}

	private String getRelativePath(File file) {
		return Repository.stripWorkDir(repo.getWorkTree(), file);
	}
}
