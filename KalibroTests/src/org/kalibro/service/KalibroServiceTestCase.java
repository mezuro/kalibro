package org.kalibro.service;

import javax.xml.ws.Endpoint;

import org.junit.Before;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.KalibroSettings;

public abstract class KalibroServiceTestCase extends KalibroTestCase {

	@Before
	public void publishEndpoints() {
		String serviceAddress = new KalibroSettings().getServiceAddress();
		Endpoint.publish(serviceAddress + "BaseToolEndpoint/", new BaseToolEndpointImpl());
		Endpoint.publish(serviceAddress + "ConfigurationEndpoint/", new ConfigurationEndpointImpl());
		Endpoint.publish(serviceAddress + "KalibroEndpoint/", new KalibroEndpointImpl());
		Endpoint.publish(serviceAddress + "ModuleResultEndpoint/", new ModuleResultEndpointImpl());
		Endpoint.publish(serviceAddress + "ProjectEndpoint/", new ProjectEndpointImpl());
		Endpoint.publish(serviceAddress + "ProjectResultEndpoint/", new ProjectResultEndpointImpl());
	}
}