package org.kalibro.desktop.settings;

import static org.junit.Assert.*;
import static org.kalibro.core.settings.SettingsFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.dialog.AbstractDialog;
import org.kalibro.desktop.swingextension.field.BooleanField;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class KalibroSettingsPanelTest extends KalibroTestCase {

	private KalibroSettings settings;

	private AbstractDialog parent;
	private KalibroSettingsPanel panel;

	private ComponentFinder finder;

	@Before
	public void setUp() {
		settings = new KalibroSettings(kalibroSettingsMap());
		parent = PowerMockito.mock(AbstractDialog.class);
		panel = new KalibroSettingsPanel(parent);
		finder = new ComponentFinder(panel);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShow() {
		panel.show(settings);
		assertEquals(settings.isClient(), clientField().get());
		assertDeepEquals(settings.getClientSettings(), clientSettingsPanel().get());
		assertDeepEquals(settings.getServerSettings(), serverSettingsPanel().get());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieve() {
		clientField().set(settings.isClient());
		clientSettingsPanel().show(settings.getClientSettings());
		serverSettingsPanel().show(settings.getServerSettings());
		assertDeepEquals(settings, panel.get());
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
		panel.show(settings);
		assertTrue(clientSettingsPanel().isVisible());
		assertFalse(serverSettingsPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldShowOnlyServerPanelWhenShowingServerSettings() {
		settings.setClient(false);
		panel.show(settings);
		assertFalse(clientSettingsPanel().isVisible());
		assertTrue(serverSettingsPanel().isVisible());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void clientAndServerPanelShouldHaveTheSameWidth() {
		assertEquals(clientSettingsPanel().getSize().width, serverSettingsPanel().getSize().width);
		assertEquals(clientSettingsPanel().getMinimumSize().width, serverSettingsPanel().getMinimumSize().width);
		assertEquals(clientSettingsPanel().getPreferredSize().width, serverSettingsPanel().getPreferredSize().width);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAdjustParentSizeWhenSwitchingPanels() {
		clientField().doClick();
		Mockito.verify(parent).adjustSize();
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