package org.kalibro.core.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.kalibro.Environment;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.yaml.snakeyaml.Yaml;

public class KalibroSettings extends AbstractEntity<KalibroSettings> {

	public static KalibroSettings load() {
		if (!settingsFileExists())
			return new KalibroSettings();
		try {
			FileInputStream settingsInputStream = new FileInputStream(settingsFile());
			return new KalibroSettings((Map<?, ?>) new Yaml().load(settingsInputStream));
		} catch (IOException exception) {
			throw new KalibroException("Could not load Kalibro settings from file: " + settingsFile(), exception);
		}
	}

	public static boolean settingsFileExists() {
		return settingsFile().exists();
	}

	private static File settingsFile() {
		return new File(Environment.dotKalibro(), "kalibro.settings");
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

	public ServerSettings getServerSettings() {
		return serverSettings;
	}

	public void setServerSettings(ServerSettings serverSettings) {
		this.serverSettings = serverSettings;
	}

	public File getLoadDirectoryFor(Project project) {
		return serverSettings.getLoadDirectoryFor(project);
	}

	public DatabaseSettings getDatabaseSettings() {
		return serverSettings.getDatabaseSettings();
	}

	public void save() {
		try {
			FileUtils.writeStringToFile(settingsFile(), toString());
		} catch (IOException exception) {
			throw new KalibroException("Could not write settings file: " + settingsFile(), exception);
		}
	}

	@Override
	public String toString() {
		return "---\nsettings: " + (client ? "CLIENT" : "SERVER") + " # " +
			"CLIENT for consuming a remote Kalibro Service; SERVER if Kalibro Service is installed on this machine\n" +
			clientSettings + serverSettings;
	}
}