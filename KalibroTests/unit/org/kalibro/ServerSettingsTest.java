package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.Environment;
import org.kalibro.tests.UnitTest;

public class ServerSettingsTest extends UnitTest {

	private static final File DEFAULT_LOAD_DIRECTORY = new File(Environment.dotKalibro(), "projects");

	private ServerSettings settings;

	@Before
	public void setUp() {
		settings = new ServerSettings();
	}

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(DEFAULT_LOAD_DIRECTORY);
	}

	@Test
	public void checkConstruction() {
		assertEquals(DEFAULT_LOAD_DIRECTORY, settings.getLoadDirectory());
		assertDeepEquals(new DatabaseSettings(), settings.getDatabaseSettings());
		assertEquals("", settings.getNotificationCommand());
	}

	@Test
	public void shouldCreateLoadDirectoryOnRetrieval() {
		assertFalse(DEFAULT_LOAD_DIRECTORY.exists());
		assertEquals(DEFAULT_LOAD_DIRECTORY, settings.getLoadDirectory());
		assertTrue(DEFAULT_LOAD_DIRECTORY.exists());
	}
}