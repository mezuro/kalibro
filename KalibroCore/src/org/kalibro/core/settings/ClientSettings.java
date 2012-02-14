package org.kalibro.core.settings;

import java.util.Map;

import org.kalibro.core.model.abstracts.AbstractEntity;

public class ClientSettings extends AbstractEntity<ClientSettings> {

	private String serviceAddress;
	private long pollingInterval;

	public ClientSettings() {
		setServiceAddress("http://localhost:8080/KalibroService/");
		setPollingInterval(5000);
	}

	public ClientSettings(Map<?, ?> settingsMap) {
		setServiceAddress("" + settingsMap.get("service_address"));
		setPollingInterval(Long.parseLong("" + settingsMap.get("polling_interval")));
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public long getPollingInterval() {
		return pollingInterval;
	}

	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	@Override
	public String toString() {
		return "\nclient:\n" +
			"    service_address: \"" + serviceAddress + "\" # Address of the remote KalibroService\n" +
			"    polling_interval: " + pollingInterval +
			" # Milliseconds between service requests for updating project state listeners\n";
	}
}