package org.kalibro.desktop.swingextension;

import static org.kalibro.RangeFixtures.*;
import static org.kalibro.RangeLabel.*;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;

import org.kalibro.desktop.ComponentWrapperDialog;

public final class InternalFrameManualTest extends JDesktopPane {

	public static void main(String[] args) throws PropertyVetoException {
		new ComponentWrapperDialog("InternalFrame", new InternalFrameManualTest()).setVisible(true);
	}

	private InternalFrameManualTest() throws PropertyVetoException {
		super();
		add(getFrame());
		setPreferredSize(new Dimension(1024, 768));
	}

	private InternalFrame<?> getFrame() throws PropertyVetoException {
		RangeFrame frame = new RangeFrame(newRange("amloc", REGULAR));
		frame.setSelected(true);
		return frame;
	}
}