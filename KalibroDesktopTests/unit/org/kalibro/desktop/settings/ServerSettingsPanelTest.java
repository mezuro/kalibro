package org.kalibro.desktop.settings;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.ServerSettings;
import org.kalibro.TestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.DirectoryField;

public class ServerSettingsPanelTest extends TestCase {

	private ServerSettings settings;

	private ServerSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new ServerSettings();
		panel = new ServerSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test
	public void shouldGet() {
		loadDirectoryField().set(settings.getLoadDirectory());
		databaseSettingsPanel().set(settings.getDatabaseSettings());
		assertDeepEquals(settings, panel.get());
	}

	@Test
	public void shouldSet() {
		panel.set(settings);
		assertEquals(settings.getLoadDirectory(), loadDirectoryField().get());
		assertDeepEquals(settings.getDatabaseSettings(), databaseSettingsPanel().get());
	}

	private DirectoryField loadDirectoryField() {
		return finder.find("loadDirectory", DirectoryField.class);
	}

	private DatabaseSettingsPanel databaseSettingsPanel() {
		return finder.find("databaseSettings", DatabaseSettingsPanel.class);
	}
}