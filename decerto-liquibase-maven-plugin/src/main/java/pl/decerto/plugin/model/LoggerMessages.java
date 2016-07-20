package pl.decerto.plugin.model;

public class LoggerMessages {

	public static final String NO_SNAPSHOTS_MSG = "No snapshots in liquibase directory - skipping";

	public static final String MOVE_DIR_MSG = "Moving changes to {0} directory";

	public static final String TEMPLATE_MSG = "Preparing empty version.changelog.xml inside snapshot directory";

	public static final String COMMIT_MSG = "Commiting liquibase directory";

	public static final String PLACEHOLDER_PATTERN = "<!--placeForVersionInclude-->";

	public static final String SNAPSHOT_CHANGELOG_INCLUDE = "<include file=\"{0}/version.changelog.xml\" relativeToChangelogFile=\"true\" />{1}    <!--placeForVersionInclude-->";

	public static final String CHANGELOG_INCLUDE_MSG = "Adding include to {0}";
}
