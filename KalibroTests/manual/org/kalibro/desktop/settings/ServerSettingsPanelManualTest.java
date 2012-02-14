package org.kalibro.desktop.settings;

import org.kalibro.core.settings.ServerSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public class ServerSettingsPanelManualTest {

	public static void main(String[] args) {
		ServerSettingsPanel panel = new ServerSettingsPanel();
		panel.show(new ServerSettings());
		new ComponentWrapperDialog("ServerSettingsPanel", panel).setVisible(true);
		System.exit(0);
	}
}