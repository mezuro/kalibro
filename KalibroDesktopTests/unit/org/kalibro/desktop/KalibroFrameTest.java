package org.kalibro.desktop;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.icon.Icon;
import org.kalibro.tests.UnitTest;

public class KalibroFrameTest extends UnitTest {

	private KalibroFrame frame;

	@Before
	public void setUp() {
		frame = new KalibroFrame();
	}

	@Test
	public void shouldExitWhenClosing() {
		assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
	}

	@Test
	public void shouldHaveKalibroIcon() {
		assertEquals(new Icon(Icon.KALIBRO).getImage(), frame.getIconImage());
	}

	@Test
	public void checkName() {
		assertEquals("kalibroFrame", frame.getName());
	}

	@Test
	public void checkMinimumSize() {
		assertEquals(new Dimension(900, 700), frame.getMinimumSize());
	}

	@Test
	public void shouldBeMaximized() {
		assertEquals(Frame.MAXIMIZED_BOTH, frame.getExtendedState());
	}

	@Test
	public void checkMenu() {
		JMenuBar menuBar = frame.getJMenuBar();
		assertEquals(1, menuBar.getMenuCount());
		assertTrue(menuBar.getMenu(0) instanceof KalibroMenu);
	}
}