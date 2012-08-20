package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.kalibro.core.Environment;

public class ServerSettingsTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		ServerSettings settings = new ServerSettings();
		assertEquals(new File(Environment.dotKalibro(), "repositories"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
	}
}