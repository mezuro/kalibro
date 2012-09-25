package org.kalibro.desktop.project;

import static org.kalibro.ProjectFixtures.*;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class ProjectFrameManualTest extends JDesktopPane {

	public static void main(String[] args) throws PropertyVetoException {
		new ComponentWrapperDialog("ProjectFrame", new ProjectFrameManualTest()).setVisible(true);
		System.exit(0);
	}

	private ProjectFrameManualTest() throws PropertyVetoException {
		super();
		add(getFrame());
		setPreferredSize(new Dimension(1024, 768));
	}

	private JInternalFrame getFrame() throws PropertyVetoException {
		ProjectFrame frame = new ProjectFrame(helloWorld());
		frame.setSelected(true);
		return frame;
	}
}