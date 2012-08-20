package org.kalibro;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.kalibro.core.Environment;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.Print;
import org.kalibro.core.settings.ClientSettings;
import org.kalibro.core.settings.ServerSettings;
import org.yaml.snakeyaml.Yaml;

/**
 * Saves and loads Kalibro preferences.
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
			return new Yaml().loadAs(settingsInputStream, KalibroSettings.class);
		} catch (Exception exception) {
			throw new KalibroException("Could not load settings from file: " + settingsFile(), exception);
		}
	}

	private static File settingsFile() {
		return new File(Environment.dotKalibro(), "kalibro.settings");
	}

	@Print(comment = "CLIENT to connect to Kalibro Service; SERVER if the service is running on this machine\n")
	private ServiceSide serviceSide;

	private ClientSettings clientSettings;
	private ServerSettings serverSettings;

	public KalibroSettings() {
		setServiceSide(ServiceSide.SERVER);
		setClientSettings(new ClientSettings());
		setServerSettings(new ServerSettings());
	}

	public boolean clientSide() {
		return serviceSide == ServiceSide.CLIENT;
	}

	public void setServiceSide(ServiceSide serviceSide) {
		this.serviceSide = serviceSide;
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

	public void save() {
		try {
			FileUtils.writeStringToFile(settingsFile(), toString());
		} catch (Exception exception) {
			throw new KalibroException("Could not save settings on file: " + settingsFile(), exception);
		}
	}
}