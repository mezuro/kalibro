package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, KalibroSettings.class})
public class KalibroSettingsTest extends UnitTest {

	private File settingsFile;

	private KalibroSettings settings;

	@Before
	public void setUp() throws Exception {
		settingsFile = mock(File.class);
		whenNew(File.class).withArguments(dotKalibro(), "kalibro.settings").thenReturn(settingsFile);
		settings = spy(new KalibroSettings());
	}

	@Test
	public void settingsExistsWhenSettingsFileExists() {
		when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.exists());

		when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.exists());
	}

	@Test
	public void shouldLoadFromSettingsFile() throws Exception {
		mockStatic(AbstractEntity.class);
		when(AbstractEntity.class, "importFrom", settingsFile, KalibroSettings.class).thenReturn(settings);
		assertSame(settings, KalibroSettings.load());
	}

	@Test
	public void checkDefaultSettings() {
		assertFalse(settings.clientSide());
		assertDeepEquals(new ClientSettings(), settings.getClientSettings());
		assertDeepEquals(new ServerSettings(), settings.getServerSettings());
	}

	@Test
	public void shouldRetrieveSide() {
		assertFalse(settings.clientSide());
		settings.setServiceSide(ServiceSide.CLIENT);
		assertTrue(settings.clientSide());
	}

	@Test
	public void shouldSaveSettingsToFile() throws Exception {
		doNothing().when(settings, "exportTo", settingsFile);
		settings.save();
		verifyPrivate(settings).invoke("exportTo", settingsFile);
	}

	@Test
	public void printingShouldBeHumanReadable() throws IOException {
		String expected = loadResource("KalibroSettings-default.yml");
		expected = expected.replace("~/.kalibro", dotKalibro().getPath());
		assertEquals(expected, new KalibroSettings().toString());
	}
}