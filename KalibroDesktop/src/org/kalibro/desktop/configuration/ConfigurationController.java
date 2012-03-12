package org.kalibro.desktop.configuration;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;

import org.kalibro.core.model.Configuration;

public class ConfigurationController extends WindowAdapter {

	private JDesktopPane desktopPane;

	public ConfigurationController(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public void newConfiguration() {
		addFrameFor(new Configuration());
	}

	public void open() {
		// TODO Auto-generated method stub

	}

	private void addFrameFor(Configuration configuration) {
		ConfigurationFrame configurationFrame = new ConfigurationFrame(configuration);
		desktopPane.add(configurationFrame);
		configurationFrame.setLocation(newLocation());
		configurationFrame.select();
	}

	private Point newLocation() {
		ConfigurationFrame selectedFrame = getSelectedFrame();
		if (selectedFrame == null)
			return new Point(0, 0);
		Point selectedLocation = selectedFrame.getLocation();
		return new Point(selectedLocation.x + 20, selectedLocation.y + 20);
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

	public void save() {
		// TODO Auto-generated method stub

	}

	public void saveAs() {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent event) {
		// TODO Auto-generated method stub
	}

	private ConfigurationFrame getSelectedFrame() {
		return (ConfigurationFrame) desktopPane.getSelectedFrame();
	}
}