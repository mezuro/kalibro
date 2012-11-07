package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.panel.ConfirmPanel;
import org.kalibro.tests.UnitTest;

public class AbstractDialogTest extends UnitTest {

	private Window owner;
	private AbstractDialog dialog;

	@Before
	public void setUp() {
		owner = new JDialog();
		dialog = new LanguageDialog(owner);
	}

	@Test
	public void shouldSetOwnerAndTitle() {
		assertSame(owner, dialog.getOwner());
		assertEquals("Languages", dialog.getTitle());
	}

	@Test
	public void shouldBeModal() {
		assertTrue(dialog.isModal());
	}

	@Test
	public void shouldDisposeOnClose() {
		assertEquals(WindowConstants.DISPOSE_ON_CLOSE, dialog.getDefaultCloseOperation());
	}

	@Test
	public void shouldCreateComponentsAndBuildPanel() {
		assertClassEquals(ConfirmPanel.class, dialog.getContentPane());
	}

	@Test
	public void shouldAdjustSize() {
		assertTrue(dialog.isMinimumSizeSet());
		assertEquals(dialog.getPreferredSize(), dialog.getMinimumSize());
		assertEquals(dialog.getPreferredSize(), dialog.getSize());
	}

	@Test
	public void shouldCentralize() {
		Point location = dialog.getLocation();
		Dimension dialogSize = dialog.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		assertEquals((screenSize.width - dialogSize.width) / 2, location.x);
		assertEquals((screenSize.height - dialogSize.height) / 2, location.y);
	}
}