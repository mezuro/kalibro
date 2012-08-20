package org.kalibro.core.settings;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.Print;

public class ClientSettings extends AbstractEntity<ClientSettings> {

	@Print(comment = "Address of the remote Kalibro Service\n")
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
}