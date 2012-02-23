package org.kalibro.desktop.settings;

import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class DatabaseSettingsPanelManualTest {

	public static void main(String[] args) {
		DatabaseSettingsPanel panel = new DatabaseSettingsPanel();
		panel.show(new DatabaseSettings());
		new ComponentWrapperDialog("DatabaseSettingsPanel", panel).setVisible(true);
	}

	private DatabaseSettingsPanelManualTest() {
		// Utility class
	}
}