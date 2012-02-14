package org.kalibro.desktop.icon;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class KalibroIconTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkScaleForInternalFrame() {
		KalibroIcon icon = new KalibroIcon();
		ImageIcon scaledIcon = icon.scaleForInternalFrame();
		assertEquals(icon.getIconWidth() * 3 / 10, scaledIcon.getIconWidth());
		assertEquals(icon.getIconHeight() * 3 / 10, scaledIcon.getIconHeight());
	}
}