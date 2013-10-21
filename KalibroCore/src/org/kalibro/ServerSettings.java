package org.kalibro;

import java.io.File;

import org.kalibro.core.Environment;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Print;

/**
 * Server side preferences.
 * 
 * @author Carlos Morais
 */
public class ServerSettings extends AbstractEntity<ServerSettings> {

	@Print(order = 1, comment = "Source code will be loaded in this directory before analysis")
	private File loadDirectory;

	private DatabaseSettings databaseSettings;

	private String notificationCommand;

	public ServerSettings() {
		setLoadDirectory(new File(Environment.dotKalibro(), "projects"));
		setDatabaseSettings(new DatabaseSettings());
		setNotificationCommand("");
	}

	public File getLoadDirectory() {
		loadDirectory.mkdirs();
		return loadDirectory;
	}

	public void setLoadDirectory(File loadDirectory) {
		this.loadDirectory = loadDirectory;
	}

	public DatabaseSettings getDatabaseSettings() {
		return databaseSettings;
	}

	public void setDatabaseSettings(DatabaseSettings databaseSettings) {
		this.databaseSettings = databaseSettings;
	}

	public String getNotificationCommand() {
		return notificationCommand;
	}

	public void setNotificationCommand(String notificationCommand) {
		this.notificationCommand = notificationCommand;
	}
}