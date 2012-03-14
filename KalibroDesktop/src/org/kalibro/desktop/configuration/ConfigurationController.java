package org.kalibro.desktop.configuration;

import static javax.swing.JOptionPane.*;

import java.awt.Point;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.kalibro.Kalibro;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.dialog.InputDialog;
import org.kalibro.desktop.swingextension.dialog.MessageDialog;

public class ConfigurationController extends InternalFrameAdapter {

	private JDesktopPane desktopPane;

	public ConfigurationController(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void newConfiguration() {
		InputDialog inputDialog = new InputDialog("New configuration", desktopPane);
		if (inputDialog.userTyped("Configuration name:"))
			newConfiguration(inputDialog.getInput());
	}

	private void newConfiguration(String name) {
		if (dao().getConfigurationNames().contains(name))
			new MessageDialog("Configuration exists", desktopPane).show("Configuration '" + name + "' already exists");
		else {
			Configuration configuration = new Configuration();
			configuration.setName(name);
			addFrameFor(configuration);
		}
	}

	public void open() {
		String chosen = chooseConfiguration("Open configuration");
		if (chosen != null)
			addFrameFor(dao().getConfiguration(chosen));
	}

	public void delete() {
		String chosen = chooseConfiguration("Delete configuration");
		if (chosen != null)
			dao().removeConfiguration(chosen);
	}

	private String chooseConfiguration(String title) {
		List<String> names = dao().getConfigurationNames();
		ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(title, desktopPane);
		if (noConfiguration(names) || !choiceDialog.choose("Select configuration:", names))
			return null;
		return choiceDialog.getChoice();
	}

	private boolean noConfiguration(List<String> configurationNames) {
		if (configurationNames.isEmpty()) {
			new MessageDialog("No configuration", desktopPane).show("No configuration found");
			return true;
		}
		return false;
	}

	public void save() {
		dao().save(selectedConfiguration());
	}

	public void saveAs() {
		InputDialog inputDialog = new InputDialog("Save configuration as...", desktopPane);
		if (!inputDialog.userTyped("Configuration name:"))
			return;
		Configuration configuration = selectedConfiguration();
		configuration.setName(inputDialog.getInput());
		dao().save(configuration);
		addFrameFor(configuration);
	}

	private void addFrameFor(Configuration configuration) {
		ConfigurationFrame configurationFrame = new ConfigurationFrame(configuration);
		desktopPane.add(configurationFrame);
		configurationFrame.addInternalFrameListener(this);
		configurationFrame.setLocation(newLocation());
		configurationFrame.select();
	}

	private Point newLocation() {
		JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
		if (selectedFrame == null)
			return new Point(0, 0);
		Point selectedLocation = selectedFrame.getLocation();
		return new Point(selectedLocation.x + 20, selectedLocation.y + 20);
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent event) {
		close();
	}

	public void close() {
		if (unmodified())
			desktopPane.getSelectedFrame().dispose();
		else
			confirmClose();
	}

	private boolean unmodified() {
		Configuration configuration = selectedConfiguration();
		String name = configuration.getName();
		boolean exists = dao().getConfigurationNames().contains(name);
		return exists && dao().getConfiguration(name).deepEquals(configuration);
	}

	private void confirmClose() {
		String name = selectedConfiguration().getName();
		JInternalFrame frame = desktopPane.getSelectedFrame();
		String message = "Configuration '" + name + "' has been modified. Save changes?";
		int answer = showConfirmDialog(frame, message, "Save configuration", YES_NO_CANCEL_OPTION);
		if (answer != CANCEL_OPTION) {
			if (answer == YES_OPTION)
				save();
			frame.dispose();
		}
	}

	private Configuration selectedConfiguration() {
		ConfigurationFrame selectedFrame = (ConfigurationFrame) desktopPane.getSelectedFrame();
		return selectedFrame.getConfiguration();
	}

	private ConfigurationDao dao() {
		return Kalibro.getConfigurationDao();
	}
}