package org.kalibro.desktop.settings;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroSettings;
import org.kalibro.ServiceSide;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class KalibroSettingsPanelTest extends UnitTest {

	private KalibroSettings settings;

	private KalibroSettingsPanel panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new KalibroSettings();
		panel = new KalibroSettingsPanel();
		finder = new ComponentFinder(panel);
	}

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(settings.getServerSettings().getLoadDirectory());
	}

	@Test
	public void shouldGet() {
		clientField().set(settings.clientSide());
		clientSettingsPanel().set(settings.getClientSettings());
		serverSettingsPanel().set(settings.getServerSettings());
		assertDeepEquals(settings, panel.get());

		clientField().set(!settings.clientSide());
		assertFalse(panel.get().deepEquals(settings));
	}

	@Test
	public void shouldSet() {
		panel.set(settings);
		assertEquals(settings.clientSide(), clientField().get());
		assertDeepEquals(settings.getClientSettings(), clientSettingsPanel().get());
		assertDeepEquals(settings.getServerSettings(), serverSettingsPanel().get());
	}

	@Test
	public void shouldSwitchPanelsWhenClientFieldChanges() {
		clientField().doClick();
		assertTrue(clientSettingsPanel().isVisible());
		assertFalse(serverSettingsPanel().isVisible());

		clientField().doClick();
		assertFalse(clientSettingsPanel().isVisible());
		assertTrue(serverSettingsPanel().isVisible());
	}

	@Test
	public void shouldShowOnlyClientPanelWhenShowingClientSettings() {
		settings.setServiceSide(ServiceSide.CLIENT);
		panel.set(settings);
		assertTrue(clientSettingsPanel().isVisible());
		assertFalse(serverSettingsPanel().isVisible());
	}

	@Test
	public void shouldShowOnlyServerPanelWhenShowingServerSettings() {
		panel.set(settings);
		assertFalse(clientSettingsPanel().isVisible());
		assertTrue(serverSettingsPanel().isVisible());
	}

	@Test
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