package org.kalibro.desktop.settings;

import org.kalibro.ServerSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ServerSettingsPanelManualTest extends ServerSettingsPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ServerSettingsPanel", new ServerSettingsPanelManualTest()).setVisible(true);
	}

	private ServerSettingsPanelManualTest() {
		super();
		set(new ServerSettings());
	}
}