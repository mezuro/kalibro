package org.kalibro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ClientSettingsTest extends UnitTest {

	@Test
	public void checkDefaultClientSettings() {
		ClientSettings settings = new ClientSettings();
		assertEquals("http://localhost:8080/KalibroService/", settings.getServiceAddress());
	}
}