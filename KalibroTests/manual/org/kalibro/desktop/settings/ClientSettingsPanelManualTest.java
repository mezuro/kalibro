package org.kalibro.desktop.settings;

import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ClientSettingsPanelManualTest {

	public static void main(String[] args) {
		ClientSettingsPanel panel = new ClientSettingsPanel();
		panel.set(new ClientSettings());
		new ComponentWrapperDialog("ClientSettingsPanel", panel).setVisible(true);
		System.exit(0);
	}

	private ClientSettingsPanelManualTest() {
		// Utility class
	}
}