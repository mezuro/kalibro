package org.kalibro.core.settings;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.kalibro.Environment;
import org.kalibro.KalibroException;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.yaml.snakeyaml.Yaml;

/**
 * Save and retrieve Kalibro settings from file.
 * 
 * @author Carlos Morais
 */
public class KalibroSettings extends AbstractEntity<KalibroSettings> {

	public static boolean exists() {
		return settingsFile().exists();
	}

	public static KalibroSettings load() {
		try {
			FileInputStream settingsInputStream = new FileInputStream(settingsFile());
			return new KalibroSettings((Map<?, ?>) new Yaml().load(settingsInputStream));
		} catch (Exception exception) {
			throw new KalibroException("Could not load settings from file: " + settingsFile(), exception);
		}
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

	public ServerSettings getServerSettings() {
		return serverSettings;
	}

	public void setServerSettings(ServerSettings serverSettings) {
		this.serverSettings = serverSettings;
	}

	public File getLoadDirectoryFor(Project project) {
		return serverSettings.getLoadDirectoryFor(project);
	}

	public void save() {
		try {
			FileUtils.writeStringToFile(settingsFile(), toString());
		} catch (Exception exception) {
			throw new KalibroException("Could not save settings on file: " + settingsFile(), exception);
		}
	}

	@Override
	public String toString() {
		return "---\nsettings: " + (client ? "CLIENT" : "SERVER") + " # " +
			"CLIENT for consuming a remote Kalibro Service; SERVER if Kalibro Service is installed on this machine\n" +
			clientSettings + serverSettings;
	}
}