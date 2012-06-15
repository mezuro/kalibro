package org.kalibro.desktop;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.configuration.ConfigurationMenu;
import org.kalibro.desktop.project.ProjectMenu;
import org.kalibro.desktop.swingextension.icon.Icon;

public class KalibroFrameTest extends KalibroTestCase {

	private KalibroFrame frame;

	@Before
	public void setUp() {
		frame = new KalibroFrame();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExitWhenClosing() {
		assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveKalibroIcon() {
		assertEquals(new Icon(Icon.KALIBRO).getImage(), frame.getIconImage());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkName() {
		assertEquals("kalibroFrame", frame.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMinimumSize() {
		assertEquals(new Dimension(900, 700), frame.getMinimumSize());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeMaximized() {
		assertEquals(Frame.MAXIMIZED_BOTH, frame.getExtendedState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkMenu() {
		JMenuBar menuBar = frame.getJMenuBar();
		assertEquals(3, menuBar.getMenuCount());
		assertTrue(menuBar.getMenu(0) instanceof KalibroMenu);
		assertTrue(menuBar.getMenu(1) instanceof ConfigurationMenu);
		assertTrue(menuBar.getMenu(2) instanceof ProjectMenu);
	}
}