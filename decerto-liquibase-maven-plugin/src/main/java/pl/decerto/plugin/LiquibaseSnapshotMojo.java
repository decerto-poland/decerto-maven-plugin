package pl.decerto.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import pl.decerto.plugin.model.LiquibaseDefaultProperties;
import pl.decerto.plugin.model.MavenProperties;
import pl.decerto.plugin.service.LiquibaseSnapshotService;
import pl.decerto.plugin.service.impl.LiquibaseSnapshotServiceImpl;

@Mojo(name = "moveSnapshots", defaultPhase = LifecyclePhase.NONE)
public class LiquibaseSnapshotMojo extends AbstractMojo {

	@Parameter(defaultValue = LiquibaseDefaultProperties.LIQUIBASE_SNAPSHOT_DIR)
	private String liquibaseDir;

	@Parameter(defaultValue = LiquibaseDefaultProperties.LIQUIBASE_DIR)
	private String liquibaseSnapshotDir;

	@Parameter(defaultValue = "${project.version}")
	private String projectVersion;

	@Parameter(defaultValue = "${basedir}")
	private String projectBaseDir;

	@Parameter(defaultValue = "${finalName}")
	private String artifactName;

	@Parameter(defaultValue = "${line.separator}")
	private String lineSeparator;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		MavenProperties mavenProperties = buildMavenProperties();
		LiquibaseSnapshotService service = new LiquibaseSnapshotServiceImpl(mavenProperties);
		service.moveSnapshots();
	}

	private MavenProperties buildMavenProperties() {
		return MavenProperties.builder()
					.liquibaseDir(formatPath(liquibaseDir))
					.liquibaseSnapshotDir(formatPath(liquibaseSnapshotDir))
					.projectBaseDir(formatPath(projectBaseDir))
					.projectVersion(projectVersion)
					.artifactName(artifactName)
					.lineSeparator(lineSeparator)
					.logger(getLog())
					.build();
	}

	private String formatPath(String path) {
		return path.replace('\\', '/');
	}
}
