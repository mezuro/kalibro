package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.kalibro.core.Environment;

public class ServerSettingsTest extends KalibroTestCase {

	private ServerSettings settings = new ServerSettings();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertEquals(new File(Environment.dotKalibro(), "repositories"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
	}
}