package org.kalibro.core.settings;

import org.kalibro.core.model.abstracts.AbstractEntity;

public class ClientSettings extends AbstractEntity<ClientSettings> {

	private String serviceAddress;

	public ClientSettings() {
		setServiceAddress("http://localhost:8080/KalibroService/");
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@Override
	public String toString() {
		return "\nclientSettings:\n" +
			"    serviceAddress: \"" + serviceAddress + "\" # Address of the remote KalibroService\n";
	}
}