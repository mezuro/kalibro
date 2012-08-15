package org.kalibro.desktop.settings;

import org.kalibro.core.settings.DatabaseSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class DatabaseSettingsPanelManualTest extends DatabaseSettingsPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("DatabaseSettingsPanel", new DatabaseSettingsPanelManualTest()).setVisible(true);
	}

	private DatabaseSettingsPanelManualTest() {
		super();
		set(new DatabaseSettings());
	}
}