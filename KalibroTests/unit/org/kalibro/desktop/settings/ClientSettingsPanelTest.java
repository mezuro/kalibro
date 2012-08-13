package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.StringField;

public class ClientSettingsPanelTest extends KalibroTestCase {

	private ClientSettings settings;

	private ClientSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new ClientSettings(clientSettingsMap());
		panel = new ClientSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		serviceAddressField().set(settings.getServiceAddress());
		assertDeepEquals(settings, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(settings);
		assertEquals(settings.getServiceAddress(), serviceAddressField().get());
	}

	private StringField serviceAddressField() {
		return finder.find("serviceAddress", StringField.class);
	}
}