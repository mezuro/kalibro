package org.kalibro.service;

import javax.xml.ws.Endpoint;

import org.junit.After;
import org.kalibro.IntegrationTest;
import org.kalibro.client.EndpointClient;

@Deprecated
public abstract class OldEndpointTest extends IntegrationTest {

	private Endpoint endpoint;

	@After
	public void tearDown() {
		endpoint.stop();
	}

	protected <T> T publishAndGetPort(T implementor, Class<T> endpointClass) {
		String serviceAddress = "http://localhost:8080/KalibroService/";
		String endpointAddress = serviceAddress + endpointClass.getSimpleName() + "/";
		endpoint = Endpoint.create(implementor);
		endpoint.publish(endpointAddress);
		return EndpointClient.getPort(serviceAddress, endpointClass);
	}
}