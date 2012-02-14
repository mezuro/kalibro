package org.kalibro.desktop.old.controllers;

import javax.swing.JDesktopPane;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.MetricConfiguration;
import org.kalibro.core.model.Range;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.desktop.old.ConfigurationFrame;

public class ConfigurationController {

	private static ConfigurationDao getDao() {
		return Kalibro.getConfigurationDao();
	}

	private ConfigurationFrame frame;

	private Configuration configuration;
	private MetricConfiguration currentMetric;
	private boolean changed;

	public ConfigurationController(JDesktopPane desktopPane, String configurationName) throws Exception {
		this(desktopPane, getDao().getConfiguration(configurationName));
	}

	public ConfigurationController(JDesktopPane desktopPane, Configuration configuration) throws Exception {
		this.configuration = configuration;
		frame = new ConfigurationFrame(this);
		frame.configuration(configuration);
		frame.showConfiguration();
		desktopPane.add(frame);
		frame.setSelected(true);
		saved();
	}

	@Override
	public void configurationSave() {
		try {
			configuration = frame.configuration();
			getDao().save(configuration);
			saved();
		} catch (Exception exception) {
			new ErrorDialog(exception).show(frame);
		}
	}

	@Override
	public void configurationSaveAs() {
		try {
			String name = userInput(frame, "Configuration name:", "Save configuration as...");
			if (name == null)
				return;
			Configuration newConfiguration = new Configuration();
			newConfiguration.setName(name);
			newConfiguration.setDescription(configuration.getDescription());
			for (ConfiguredMetric metric : configuration.getMetrics())
				newConfiguration.addMetric(metric);
			getDao().save(newConfiguration);
			configuration = newConfiguration;
			frame.configuration(configuration);
			frame.showConfiguration();
			saved();
		} catch (Exception exception) {
			new ErrorDialog(exception).show(frame);
		}
	}

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