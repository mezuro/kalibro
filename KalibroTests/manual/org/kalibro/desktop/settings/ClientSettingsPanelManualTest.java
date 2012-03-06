package org.kalibro.desktop.settings;

import org.kalibro.core.settings.ClientSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ClientSettingsPanelManualTest extends ClientSettingsPanel {

	public static void main(String[] args) {
		new ComponentWrapperDialog("ClientSettingsPanel", new ClientSettingsPanelManualTest()).setVisible(true);
	}

	private ClientSettingsPanelManualTest() {
		super();
		set(new ClientSettings());
	}
}