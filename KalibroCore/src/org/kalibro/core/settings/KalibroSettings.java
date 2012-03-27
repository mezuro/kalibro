package org.kalibro.core.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.util.Directories;
import org.yaml.snakeyaml.Yaml;

public class KalibroSettings extends AbstractEntity<KalibroSettings> {

	private static final File SETTINGS_FILE = new File(Directories.kalibro(), "kalibro.settings");

	public static KalibroSettings load() {
		if (!settingsFileExists())
			return new KalibroSettings();
		try {
			return new KalibroSettings((Map<?, ?>) new Yaml().load(new FileInputStream(SETTINGS_FILE)));
		} catch (IOException exception) {
			throw new KalibroException("Could not load Kalibro settings from file: " + SETTINGS_FILE, exception);
		}
	}

	public static boolean settingsFileExists() {
		return SETTINGS_FILE.exists();
	}

	private boolean client;
	private ClientSettings clientSettings;
	private ServerSettings serverSettings;

	public KalibroSettings() {
		setClient(false);
		setClientSettings(new ClientSettings());
		setServerSettings(new ServerSettings());
	}

	public KalibroSettings(Map<?, ?> settingsMap) {
		setClient(settingsMap.get("settings").equals("CLIENT"));
		setClientSettings(new ClientSettings((Map<?, ?>) settingsMap.get("client")));
		setServerSettings(new ServerSettings((Map<?, ?>) settingsMap.get("server")));
	}

	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}

	public ClientSettings getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(ClientSettings clientSettings) {
		this.clientSettings = clientSettings;
	}

	public String getServiceAddress() {
		return clientSettings.getServiceAddress();
	}

	public long getPollingInterval() {
		return clientSettings.getPollingInterval();
	}

	public ServerSettings getServerSettings() {
		return serverSettings;
	}

	public void setServerSettings(ServerSettings serverSettings) {
		this.serverSettings = serverSettings;
	}

	public File getLoadDirectoryFor(Project project) {
		return serverSettings.getLoadDirectoryFor(project);
	}

	public boolean shouldRemoveSources() {
		return serverSettings.shouldRemoveSources();
	}

	public DatabaseSettings getDatabaseSettings() {
		return serverSettings.getDatabaseSettings();
	}

	public void write() {
		try {
			FileUtils.writeStringToFile(SETTINGS_FILE, toString());
		} catch (IOException exception) {
			throw new KalibroException("Could not write settings file: " + SETTINGS_FILE, exception);
		}
	}

	@Override
	public String toString() {
		return "---\nsettings: " + (client ? "CLIENT" : "SERVER") + " # " +
			"CLIENT for consuming a remote Kalibro Service; SERVER if Kalibro Service is installed on this machine\n" +
			clientSettings + serverSettings;
	}
}