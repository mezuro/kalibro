package org.kalibro.desktop;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class KalibroFrameTest extends KalibroTestCase {

	private KalibroFrameListener listener;

	private KalibroFrame frame;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		listener = PowerMockito.mock(KalibroFrameListener.class);
		frame = new KalibroFrame(listener);
		finder = new ComponentFinder(frame);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMinimumSize() {
		assertEquals(new Dimension(800, 540), frame.getMinimumSize());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultSizeShouldBeScreenSize() {
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize(), frame.getSize());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExitWhenClosing() {
		assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldEditSettings() {
		menuItem("settings").doClick();
		Mockito.verify(listener).editSettings();
	}

	private JMenuItem menuItem(String name) {
		return finder.find(name, JMenuItem.class);
	}
}