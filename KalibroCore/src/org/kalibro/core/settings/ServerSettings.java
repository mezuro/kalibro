package org.kalibro.core.settings;

import java.io.File;

import org.kalibro.core.Environment;
import org.kalibro.core.model.abstracts.AbstractEntity;

public class ServerSettings extends AbstractEntity<ServerSettings> {

	private File loadDirectory;
	private DatabaseSettings databaseSettings;

	public ServerSettings() {
		setLoadDirectory(new File(Environment.dotKalibro(), "projects"));
		setDatabaseSettings(new DatabaseSettings());
	}

	public File getLoadDirectory() {
		return loadDirectory;
	}

	public void setLoadDirectory(File loadDirectory) {
		this.loadDirectory = loadDirectory;
		loadDirectory.mkdirs();
	}

	public DatabaseSettings getDatabaseSettings() {
		return databaseSettings;
	}

	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		this.databaseSettings = databaseSettings;
	}

	@Override
	public String toString() {
		return "\nserverSettings:\n" +
			"    loadDirectory: " + loadDirectory.getAbsolutePath() +
			" # Projects will be loaded in this directory before analysis\n" +
			databaseSettings;
	}
}