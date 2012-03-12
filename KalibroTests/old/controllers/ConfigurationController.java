package org.kalibro.desktop.old.controllers;

import javax.swing.JDesktopPane;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.desktop.old.ConfigurationFrame;

public class ConfigurationController {

	@Override
	public void frameClosing() {
		if (! changed) {
			frame.dispose();
			return;
		}

		String message = "Configuration '" + configuration.getName() + "' has been modified. Save changes?";
		int answer = yesNoCancelConfirmation(frame, message, "Save configuration");
		if (answer != JOptionPane.CANCEL_OPTION) {
			if (answer == JOptionPane.YES_OPTION)
				configurationSave();
			frame.dispose();
		}
	}
}