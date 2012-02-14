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
		panel.show(settings);
		assertEquals(settings.getLoadDirectory(), loadDirectoryField().getDirectory());
		assertEquals(settings.shouldRemoveSources(), removeSourcesField().getValue());
		assertDeepEquals(settings.getDatabaseSettings(), databaseSettingsPanel().retrieve());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		loadDirectoryField().setDirectory(settings.getLoadDirectory());
		removeSourcesField().setValue(settings.shouldRemoveSources());
		databaseSettingsPanel().show(settings.getDatabaseSettings());
		assertDeepEquals(settings, panel.retrieve());
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