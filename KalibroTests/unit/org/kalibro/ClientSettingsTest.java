package org.kalibro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClientSettingsTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultClientSettings() {
		ClientSettings settings = new ClientSettings();
		assertEquals("http://localhost:8080/KalibroService/", settings.getServiceAddress());
	}
}