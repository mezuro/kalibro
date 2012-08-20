package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.Task;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.yaml.snakeyaml.Yaml;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, KalibroSettings.class})
public class KalibroSettingsTest extends KalibroTestCase {

	private Yaml yaml;
	private File settingsFile;
	private FileInputStream inputStream;

	private KalibroSettings settings;

	@Before
	public void setUp() throws Exception {
		yaml = mock(Yaml.class);
		settingsFile = mock(File.class);
		inputStream = mock(FileInputStream.class);
		whenNew(Yaml.class).withNoArguments().thenReturn(yaml);
		whenNew(FileInputStream.class).withArguments(settingsFile).thenReturn(inputStream);
		whenNew(File.class).withArguments(dotKalibro(), "kalibro.settings").thenReturn(settingsFile);
		mockStatic(FileUtils.class);
		settings = new KalibroSettings();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void settingsExistsWhenSettingsFileExists() {
		when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.exists());

		when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.exists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadFromSettingsFile() {
		when(yaml.loadAs(inputStream, KalibroSettings.class)).thenReturn(settings);
		assertSame(settings, KalibroSettings.load());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionWhenCantLoadSettings() {
		when(yaml.loadAs(inputStream, KalibroSettings.class)).thenThrow(new NullPointerException());
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				KalibroSettings.load();
			}
		}, "Could not load settings from file: " + settingsFile, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultSettings() {
		assertFalse(settings.clientSide());
		assertDeepEquals(new ClientSettings(), settings.getClientSettings());
		assertDeepEquals(new ServerSettings(), settings.getServerSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveSide() {
		assertFalse(settings.clientSide());
		settings.setServiceSide(ServiceSide.CLIENT);
		assertTrue(settings.clientSide());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveSettingsToFile() throws IOException {
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
		}, "Could not save settings on file: " + settingsFile, IOException.class);
	}
}