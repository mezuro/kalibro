package org.kalibro.core.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.internal.verification.VerificationModeFactory.*;
import static org.powermock.api.mockito.PowerMockito.*;

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
		settingsFile = mock(File.class);
		Whitebox.setInternalState(KalibroSettings.class, settingsFile);
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
	public void testServiceAddress() {
		settings.getClientSettings().setServiceAddress("42");
		assertSame("42", settings.getServiceAddress());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoadPathForProject() {
		Project project = mock(Project.class);
		ServerSettings serverSettings = mock(ServerSettings.class);
		when(serverSettings.getLoadDirectoryFor(project)).thenReturn(helloWorldDirectory());

		settings.setServerSettings(serverSettings);
		assertSame(helloWorldDirectory(), settings.getLoadDirectoryFor(project));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testDatabaseSettings() {
		assertSame(settings.getServerSettings().getDatabaseSettings(), settings.getDatabaseSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSettingsFileExists() {
		when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.settingsFileExists());

		when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.settingsFileExists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() throws IOException {
		InputStream resource = getClass().getResourceAsStream("default.settings");
		String expected = IOUtils.toString(resource).replace("~", System.getProperty("user.home"));
		assertEquals(expected, settings.toString());

		settings.setClient(true);
		expected = expected.replace("settings: SERVER", "settings: CLIENT");
		assertEquals(expected, settings.toString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testWriteSettings() throws IOException {
		settings.write();
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
				settings.write();
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
	public void loadShouldReturnDefaultSettingsWhenFileDoesNotExist() throws Exception {
		prepareForLoad(false);
		assertDeepEquals(new KalibroSettings(), KalibroSettings.load());

		verifyStatic(times(0));
		FileUtils.writeStringToFile(any(File.class), anyString());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testLoadError() throws Exception {
		prepareForLoad(true);
		whenNew(FileInputStream.class).withArguments(settingsFile).thenThrow(new IOException());
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