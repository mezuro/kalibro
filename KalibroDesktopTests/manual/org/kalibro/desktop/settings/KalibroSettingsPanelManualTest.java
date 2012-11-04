package org.kalibro.desktop.settings;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.kalibro.KalibroSettings;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

public final class KalibroSettingsPanelManualTest extends ComponentWrapperDialog {

	public static void main(String[] args) {
		KalibroSettingsPanel panel = new KalibroSettingsPanel();
		panel.set(new KalibroSettings());
		new KalibroSettingsPanelManualTest(panel);
	}

	private KalibroSettingsPanelManualTest(KalibroSettingsPanel panel) {
		super("KalibroSettingsPanel", panel);
		panel.addComponentListener(new PanelListener());
		setVisible(true);
	}

	private class PanelListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent event) {
			adjustSize();
		}
	}
}