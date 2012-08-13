package org.kalibro.core.settings;

import java.util.Map;

import org.kalibro.core.model.abstracts.AbstractEntity;

public class ClientSettings extends AbstractEntity<ClientSettings> {

	private String serviceAddress;

	public ClientSettings() {
		setServiceAddress("http://localhost:8080/KalibroService/");
	}

	public ClientSettings(Map<?, ?> settingsMap) {
		setServiceAddress("" + settingsMap.get("service_address"));
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@Override
	public String toString() {
		return "\nclient:\n" +
			"    service_address: \"" + serviceAddress + "\" # Address of the remote KalibroService\n";
	}
}