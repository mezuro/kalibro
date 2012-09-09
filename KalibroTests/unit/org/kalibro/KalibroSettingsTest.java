package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractEntity.class, KalibroSettings.class})
public class KalibroSettingsTest extends TestCase {

	private File settingsFile;

	private KalibroSettings settings;

	@Before
	public void setUp() throws Exception {
		settingsFile = mock(File.class);
		whenNew(File.class).withArguments(dotKalibro(), "kalibro.settings").thenReturn(settingsFile);
		mockStatic(AbstractEntity.class);
		settings = spy(new KalibroSettings());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void settingsExistsWhenSettingsFileExists() {
		when(settingsFile.exists()).thenReturn(true);
		assertTrue(KalibroSettings.exists());

		when(settingsFile.exists()).thenReturn(false);
		assertFalse(KalibroSettings.exists());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadFromSettingsFile() throws Exception {
		when(AbstractEntity.class, "importFrom", settingsFile, KalibroSettings.class).thenReturn(settings);
		assertSame(settings, KalibroSettings.load());
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
	public void shouldSaveSettingsToFile() {
		doNothing().when(settings).exportTo(settingsFile);
		settings.save();
		verify(settings).exportTo(settingsFile);
	}
}