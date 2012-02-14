package org.kalibro.desktop.settings;

import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public class ClientSettingsPanelManualTest {

	public static void main(String[] args) {
		ClientSettingsPanel panel = new ClientSettingsPanel();
		panel.show(new ClientSettings());
		new ComponentWrapperDialog("ClientSettingsPanel", panel).setVisible(true);
		System.exit(0);
	}
}