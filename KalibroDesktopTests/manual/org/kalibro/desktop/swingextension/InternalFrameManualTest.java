package org.kalibro.desktop.swingextension;

import static org.kalibro.tests.UnitTest.loadFixture;

import java.awt.Dimension;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;

import org.kalibro.Range;
import org.kalibro.desktop.tests.ComponentWrapperDialog;

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
		RangeFrame frame = new RangeFrame(loadFixture("lcom4-bad", Range.class));
		frame.setSelected(true);
		return frame;
	}
}