package org.kalibro.desktop.settings;

import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class KalibroSettingsPanelManualTest {

	public static void main(String[] args) {
		ComponentWrapperDialog dialog = new ComponentWrapperDialog("KalibroSettingsPanel");
		KalibroSettingsPanel panel = new KalibroSettingsPanel(dialog);
		panel.show(new KalibroSettings());
		dialog.setComponent(panel);
		dialog.setVisible(true);
	}

	private KalibroSettingsPanelManualTest() {
		// Utility class
	}
}