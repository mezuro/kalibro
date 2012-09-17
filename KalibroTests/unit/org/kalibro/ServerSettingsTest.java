package org.kalibro;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.kalibro.core.Environment;

public class ServerSettingsTest extends TestCase {

	@Test
	public void checkDefaultSettings() {
		ServerSettings settings = new ServerSettings();
		assertEquals(new File(Environment.dotKalibro(), "repositories"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
	}
}