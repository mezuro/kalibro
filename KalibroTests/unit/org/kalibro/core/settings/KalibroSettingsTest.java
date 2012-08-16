package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.*;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Environment;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.yaml.snakeyaml.Yaml;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, KalibroSettings.class})
public class KalibroSettingsTest extends KalibroTestCase {

	private File settingsFile;
	private KalibroSettings settings;

	@Before
	public void setUp() throws Exception {
		settings = new KalibroSettings();
		settingsFile = mock(File.class);
		whenNew(File.class).withArguments(Environment.dotKalibro(), "kalibro.settings").thenReturn(settingsFile);
		mockStatic(FileUtils.class);
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
	public void testLoadPathForProject() {
		Project project = mock(Project.class);
		ServerSettings serverSettings = mock(ServerSettings.class);
		File directory = helloWorldDirectory();
		when(serverSettings.getLoadDirectoryFor(project)).thenReturn(directory);

		settings.setServerSettings(serverSettings);
		assertSame(directory, settings.getLoadDirectoryFor(project));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDatabaseSettings() {
		assertSame(settings.getServerSettings().getDatabaseSettings(), settings.getDatabaseSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSettingsFileExists() {
		when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.exists());

		when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.exists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() throws IOException {
		InputStream resource = getClass().getResourceAsStream("default.settings");
		String expected = IOUtils.toString(resource).replace("~/.kalibro", Environment.dotKalibro().getPath());
		assertEquals(expected, settings.toString());

		settings.setClient(true);
		expected = expected.replace("settings: SERVER", "settings: CLIENT");
		assertEquals(expected, settings.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testWriteSettings() throws IOException {
		settings.save();
		verifyStatic();
		FileUtils.writeStringToFile(settingsFile, settings.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorWritingFile() throws Exception {
		doThrow(new IOException()).when(FileUtils.class);
		FileUtils.writeStringToFile(settingsFile, settings.toString());
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				settings.save();
			}
		}, "Could not write settings file: " + settingsFile, IOException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoad() throws Exception {
		prepareForLoad(true);
		KalibroSettings expected = new KalibroSettings(SettingsFixtures.kalibroSettingsMap());
		assertDeepEquals(expected, KalibroSettings.load());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionWhenTryingToLoadInexistentSettings() throws Exception {
		whenNew(FileInputStream.class).withArguments(settingsFile).thenThrow(new FileNotFoundException());
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				KalibroSettings.load();
			}
		}, "There is no settings to load.", FileNotFoundException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionWhenSettingsFileIsCorrupted() throws Exception {
		Yaml yaml = mock(Yaml.class);
		FileInputStream inputStream = mock(FileInputStream.class);
		whenNew(Yaml.class).withNoArguments().thenReturn(yaml);
		whenNew(FileInputStream.class).withArguments(settingsFile).thenReturn(inputStream);
		when(yaml.load(inputStream)).thenReturn(SettingsFixtures.kalibroSettingsMap());
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				KalibroSettings.load();
			}
		}, "Could not load Kalibro settings from file: " + settingsFile, IOException.class);
	}

	private void prepareForLoad(boolean settingsFileExists) throws Exception {
		when(settingsFile.exists()).thenReturn(settingsFileExists);

		Yaml yaml = mock(Yaml.class);
		FileInputStream inputStream = mock(FileInputStream.class);
		whenNew(Yaml.class).withNoArguments().thenReturn(yaml);
		whenNew(FileInputStream.class).withArguments(settingsFile).thenReturn(inputStream);
		when(yaml.load(inputStream)).thenReturn(SettingsFixtures.kalibroSettingsMap());
	}
}