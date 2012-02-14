package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.yaml.snakeyaml.Yaml;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, KalibroSettings.class})
public class KalibroSettingsTest extends KalibroTestCase {

	private File settingsFile;
	private KalibroSettings settings;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		settingsFile = PowerMockito.mock(File.class);
		Whitebox.setInternalState(KalibroSettings.class, settingsFile);
		PowerMockito.mockStatic(FileUtils.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultLocalSettings() {
		assertFalse(settings.isClient());
		assertDeepEquals(new ClientSettings(), settings.getClientSettings());
		assertDeepEquals(new ServerSettings(), settings.getServerSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMapConstructor() {
		Map<?, ?> map = kalibroSettingsMap();
		settings = new KalibroSettings(map);
		assertTrue(settings.isClient());
		assertDeepEquals(new ClientSettings(clientSettingsMap()), settings.getClientSettings());
		assertDeepEquals(new ServerSettings(serverSettingsMap()), settings.getServerSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testServiceAddress() {
		settings.getClientSettings().setServiceAddress("42");
		assertSame("42", settings.getServiceAddress());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testPollingInterval() {
		settings.getClientSettings().setPollingInterval(42);
		assertEquals(42L, settings.getPollingInterval());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoadPathForProject() {
		Project project = PowerMockito.mock(Project.class);
		ServerSettings serverSettings = PowerMockito.mock(ServerSettings.class);
		PowerMockito.when(serverSettings.getLoadDirectoryFor(project)).thenReturn(HELLO_WORLD_DIRECTORY);

		settings.setServerSettings(serverSettings);
		assertSame(HELLO_WORLD_DIRECTORY, settings.getLoadDirectoryFor(project));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShouldRemoveSources() {
		settings.getServerSettings().setRemoveSources(false);
		assertFalse(settings.shouldRemoveSources());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDatabaseSettings() {
		assertSame(settings.getServerSettings().getDatabaseSettings(), settings.getDatabaseSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSettingsFileExists() {
		PowerMockito.when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.settingsFileExists());

		PowerMockito.when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.settingsFileExists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() throws IOException {
		InputStream resource = getClass().getResourceAsStream("default.settings");
		String expected = IOUtils.toString(resource).replace("~", System.getProperty("user.home"));
		assertEquals(expected, settings.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testWriteSettings() throws IOException {
		settings.write();
		PowerMockito.verifyStatic();
		FileUtils.writeStringToFile(settingsFile, settings.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorWritingFile() throws Exception {
		IOException error = new IOException();
		PowerMockito.doThrow(error).when(FileUtils.class);
		FileUtils.writeStringToFile(settingsFile, settings.toString());
		try {
			settings.write();
			fail("Preceding line should throw Exception");
		} catch (RuntimeException exception) {
			assertSame(error, exception.getCause());
			assertEquals("Could not write settings file", exception.getMessage());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoad() throws Exception {
		prepareForLoad(true);
		KalibroSettings expected = new KalibroSettings(SettingsFixtures.kalibroSettingsMap());
		assertDeepEquals(expected, KalibroSettings.load());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void loadShouldReturnDefaultSettingsWhenFileDoesNotExist() throws Exception {
		prepareForLoad(false);
		assertDeepEquals(new KalibroSettings(), KalibroSettings.load());

		PowerMockito.verifyStatic(never());
		FileUtils.writeStringToFile(any(File.class), anyString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoadError() throws Exception {
		prepareForLoad(true);
		PowerMockito.whenNew(FileInputStream.class).withArguments(settingsFile).thenThrow(new IOException());
		checkException(new Task() {

			@Override
			public void perform() {
				KalibroSettings.load();
			}
		}, RuntimeException.class, "Could not load Kalibro settings", IOException.class);
	}

	private void prepareForLoad(boolean settingsFileExists) throws Exception {
		PowerMockito.when(settingsFile.exists()).thenReturn(settingsFileExists);

		Yaml yaml = PowerMockito.mock(Yaml.class);
		FileInputStream inputStream = PowerMockito.mock(FileInputStream.class);
		PowerMockito.whenNew(Yaml.class).withNoArguments().thenReturn(yaml);
		PowerMockito.whenNew(FileInputStream.class).withArguments(settingsFile).thenReturn(inputStream);
		PowerMockito.when(yaml.load(inputStream)).thenReturn(SettingsFixtures.kalibroSettingsMap());
	}
}