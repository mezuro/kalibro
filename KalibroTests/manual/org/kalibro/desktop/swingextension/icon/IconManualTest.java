package org.kalibro.desktop.swingextension.icon;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class IconManualTest extends JDesktopPane {

	public static void main(String[] args) throws PropertyVetoException {
		new ComponentWrapperDialog("IconManualTest", new IconManualTest()).setVisible(true);
	}

	private IconManualTest() throws PropertyVetoException {
		super();
		add(getFrame());
		setPreferredSize(new Dimension(640, 480));
	}

	private JInternalFrame getFrame() throws PropertyVetoException {
		JInternalFrame frame = new JInternalFrame("IconManualTest");
		new Icon(Icon.KALIBRO).replaceIconOf(frame);
		frame.setVisible(true);
		frame.setSelected(true);
		frame.setSize(480, 300);
		return frame;
	}
}