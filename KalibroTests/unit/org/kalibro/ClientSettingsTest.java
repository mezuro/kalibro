package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientSettingsTest extends KalibroTestCase {

	private ClientSettings settings = new ClientSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultClientSettings() {
		assertEquals("http://localhost:8080/KalibroService/", settings.getServiceAddress());
	}
}