package org.kalibro.core.settings;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ClientSettingsTest extends KalibroTestCase {

	private ClientSettings settings = new ClientSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultClientSettings() {
		assertEquals("http://localhost:8080/KalibroService/", settings.getServiceAddress());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMapConstructor() {
		Map<?, ?> map = SettingsFixtures.clientSettingsMap();
		settings = new ClientSettings(map);
		assertEquals(map.get("service_address"), settings.getServiceAddress());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkToString() throws IOException {
		String expected = IOUtils.toString(getClass().getResourceAsStream("client.settings"));
		assertEquals(expected, "" + settings);
	}
}