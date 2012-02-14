package org.kalibro.desktop.icon;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class AbstractIconTest extends KalibroTestCase {

	private AbstractIcon icon;

	@Before
	public void setUp() {
		icon = new MethodIcon();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidatePresenceOfResource() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				icon = new AbstractIcon("inexistent_file.gif") {/* just for checking */};
			}
		}, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkScale() {
		ImageIcon scaledIcon = icon.scale(2);
		assertEquals(2 * icon.getIconWidth(), scaledIcon.getIconWidth());
		assertEquals(2 * icon.getIconHeight(), scaledIcon.getIconHeight());
	}
}