package org.kalibro.core.settings;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KalibroSettings.class)
public class SettingsFileTest extends KalibroTestCase {

	private File settingsFile;
	private KalibroSettings settings;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		settingsFile = new File(TESTS_DIRECTORY, "kalibro.settings");
		Whitebox.setInternalState(KalibroSettings.class, settingsFile);
	}

	@After
	public void tearDown() {
		settingsFile.delete();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testSettingsFileExists() throws IOException {
		assertFalse(KalibroSettings.settingsFileExists());
		settingsFile.createNewFile();
		assertTrue(KalibroSettings.settingsFileExists());
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testWriteSettings() throws IOException {
		settings.write();
		assertEquals(settings.toString(), FileUtils.readFileToString(settingsFile));
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testWriteAndLoad() {
		settings.write();
		assertDeepEquals(settings, KalibroSettings.load());
	}

	@Test(timeout = INTEGRATION_TIMEOUT, expected = NullPointerException.class)
	public void checkErrorLoadingFile() throws IOException {
		settingsFile.createNewFile();
		KalibroSettings.load();
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void loadShouldReturnDefaultSettingsWhenFileDoesNotExist() {
		assertDeepEquals(new KalibroSettings(), KalibroSettings.load());
		assertFalse(settingsFile.exists());
	}
}