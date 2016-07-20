package pl.decerto.plugin.git;

import java.io.File;

public interface GitService {
	void commitDirectory(File commitDir);
}
