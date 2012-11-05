package org.kalibro.desktop.settings;

import org.kalibro.DatabaseSettings;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class DatabaseSettingsPanelManualTest extends DatabaseSettingsPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("DatabaseSettingsPanel", new DatabaseSettingsPanelManualTest()).setVisible(true);
	}

	private DatabaseSettingsPanelManualTest() {
		super();
		set(new DatabaseSettings());
	}
}