package org.kalibro.core.settings;

import java.io.File;
import java.util.Map;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.util.Directories;

public class ServerSettings extends AbstractEntity<ServerSettings> {

	private File loadDirectory;
	private boolean removeSources;
	private DatabaseSettings databaseSettings;

	public ServerSettings() {
		setLoadDirectory(new File(Directories.kalibro(), "projects"));
		setRemoveSources(true);
		setDatabaseSettings(new DatabaseSettings());
	}

	public ServerSettings(Map<?, ?> settingsMap) {
		setLoadDirectory(new File("" + settingsMap.get("load_directory")));
		setRemoveSources(Boolean.parseBoolean("" + settingsMap.get("remove_sources")));
		setDatabaseSettings(new DatabaseSettings((Map<?, ?>) settingsMap.get("database")));
	}

	public File getLoadDirectoryFor(Project project) {
		return new File(loadDirectory, project.getName().replace(' ', '_'));
	}

	public File getLoadDirectory() {
		return loadDirectory;
	}

	public void setLoadDirectory(File loadDirectory) {
		this.loadDirectory = loadDirectory;
		loadDirectory.mkdirs();
	}

	public boolean shouldRemoveSources() {
		return removeSources;
	}

	public void setRemoveSources(boolean removeSources) {
		this.removeSources = removeSources;
	}

	public DatabaseSettings getDatabaseSettings() {
		return databaseSettings;
	}

	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		this.databaseSettings = databaseSettings;
	}

	@Override
	public String toString() {
		return "\nserver:\n" +
			"    load_directory: " + loadDirectory.getAbsolutePath() +
			" # Projects will be loaded in this directory before analysis\n" +
			"    remove_sources: " + removeSources +
			" # If true, removes analyzed source code from the directory specified in the previous line\n" +
			databaseSettings;
	}
}