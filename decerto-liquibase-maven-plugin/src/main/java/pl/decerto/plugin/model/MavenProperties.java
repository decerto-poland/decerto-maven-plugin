package pl.decerto.plugin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.maven.plugin.logging.Log;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MavenProperties {

	private String liquibaseDir;

	private String liquibaseSnapshotDir;

	private String projectVersion;

	private String projectBaseDir;

	private String artifactName;

	private String lineSeparator;

	private Log logger;
}
