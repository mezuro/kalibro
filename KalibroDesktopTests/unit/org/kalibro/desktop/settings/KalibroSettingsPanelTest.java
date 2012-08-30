package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroSettings;
import org.kalibro.ServiceSide;
import org.kalibro.TestCase;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.field.BooleanField;

public class KalibroSettingsPanelTest extends TestCase {

	private KalibroSettings settings;

	private KalibroSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		panel = new KalibroSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGet() {
		clientField().set(settings.clientSide());
		clientSettingsPanel().set(settings.getClientSettings());
		serverSettingsPanel().set(settings.getServerSettings());
		assertDeepEquals(settings, panel.get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSet() {
		panel.set(settings);
		assertEquals(settings.clientSide(), clientField().get());
		assertDeepEquals(settings.getClientSettings(), clientSettingsPanel().get());
		assertDeepEquals(settings.getServerSettings(), serverSettingsPanel().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSwitchPanelsWhenClientFieldChanges() {
		clientField().doClick();
		assertTrue(clientSettingsPanel().isVisible());
		assertFalse(serverSettingsPanel().isVisible());

		clientField().doClick();
		assertFalse(clientSettingsPanel().isVisible());
		assertTrue(serverSettingsPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowOnlyClientPanelWhenShowingClientSettings() {
		settings.setServiceSide(ServiceSide.CLIENT);
		panel.set(settings);
		assertTrue(clientSettingsPanel().isVisible());
		assertFalse(serverSettingsPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowOnlyServerPanelWhenShowingServerSettings() {
		panel.set(settings);
		assertFalse(clientSettingsPanel().isVisible());
		assertTrue(serverSettingsPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void clientAndServerPanelShouldHaveTheSameWidth() {
		assertEquals(clientSettingsPanel().getSize().width, serverSettingsPanel().getSize().width);
		assertEquals(clientSettingsPanel().getMinimumSize().width, serverSettingsPanel().getMinimumSize().width);
		assertEquals(clientSettingsPanel().getPreferredSize().width, serverSettingsPanel().getPreferredSize().width);
	}

	private BooleanField clientField() {
		return finder.find("client", BooleanField.class);
	}

	private ClientSettingsPanel clientSettingsPanel() {
		return finder.find("clientSettings", ClientSettingsPanel.class);
	}

	private ServerSettingsPanel serverSettingsPanel() {
		return finder.find("serverSettings", ServerSettingsPanel.class);
	}
}