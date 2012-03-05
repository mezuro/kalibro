package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.ServerSettings;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.desktop.swingextension.field.DirectoryField;

public class ServerSettingsPanelTest extends KalibroTestCase {

	private ServerSettings settings;

	private ServerSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new ServerSettings(serverSettingsMap());
		panel = new ServerSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.set(settings);
		assertEquals(settings.getLoadDirectory(), loadDirectoryField().get());
		assertEquals(settings.shouldRemoveSources(), removeSourcesField().get());
		assertDeepEquals(settings.getDatabaseSettings(), databaseSettingsPanel().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		loadDirectoryField().set(settings.getLoadDirectory());
		removeSourcesField().set(settings.shouldRemoveSources());
		databaseSettingsPanel().set(settings.getDatabaseSettings());
		assertDeepEquals(settings, panel.get());
	}

	private DirectoryField loadDirectoryField() {
		return finder.find("loadDirectory", DirectoryField.class);
	}

	private BooleanField removeSourcesField() {
		return finder.find("removeSources", BooleanField.class);
	}

	private DatabaseSettingsPanel databaseSettingsPanel() {
		return finder.find("databaseSettings", DatabaseSettingsPanel.class);
	}
}