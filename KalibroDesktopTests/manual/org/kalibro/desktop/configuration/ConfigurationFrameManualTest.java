package org.kalibro.desktop.configuration;

import static org.kalibro.core.model.ConfigurationFixtures.*;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.kalibro.core.model.Configuration;
import org.kalibro.desktop.ComponentWrapperDialog;

public final class ConfigurationFrameManualTest extends JDesktopPane {

	public static void main(String[] args) throws PropertyVetoException {
		new ComponentWrapperDialog("ConfigurationFrame", new ConfigurationFrameManualTest()).setVisible(true);
	}

	private ConfigurationFrameManualTest() throws PropertyVetoException {
		super();
		add(getFrame());
		setPreferredSize(new Dimension(1024, 768));
	}

	private JInternalFrame getFrame() throws PropertyVetoException {
		Configuration configuration = kalibroConfiguration();
		ConfigurationFrame frame = new ConfigurationFrame(configuration);
		frame.setSelected(true);
		return frame;
	}
}