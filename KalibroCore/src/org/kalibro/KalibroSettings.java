package org.kalibro;

import java.io.File;

import org.kalibro.core.Environment;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.Print;

/**
 * Save and retrieve Kalibro preferences.
 * 
 * @author Carlos Morais
 */
public class KalibroSettings extends AbstractEntity<KalibroSettings> {

	public static boolean exists() {
		return settingsFile().exists();
	}

	public static KalibroSettings load() {
		return importFrom(settingsFile(), KalibroSettings.class);
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
		exportTo(settingsFile());
	}
}