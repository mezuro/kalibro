package org.kalibro;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.kalibro.core.Environment;
import org.kalibro.tests.UnitTest;

public class ServerSettingsTest extends UnitTest {

	@Test
	public void checkConstruction() {
		ServerSettings settings = new ServerSettings();
		assertEquals(new File(Environment.dotKalibro(), "projects"), settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
	}
}