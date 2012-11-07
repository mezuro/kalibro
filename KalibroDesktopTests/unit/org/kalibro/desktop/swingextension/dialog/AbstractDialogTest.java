package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;
import org.powermock.reflect.Whitebox;

public class AbstractDialogTest extends UnitTest {

	private static final String TITLE = "AbstractDialogTest";
	private static final JPanel PANEL = new JPanel();

	private Window owner;
	private DialogMock dialog;
	private boolean createdComponents, showed;

	@Before
	public void setUp() {
		owner = new JDialog();
		createdComponents = false;
		showed = false;
		dialog = new DialogMock();
	}

	@Test
	public void shouldSetOwnerAndTitle() {
		assertSame(TITLE, dialog.getTitle());
		assertSame(owner, dialog.getOwner());
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
	public void shouldCreateComponents() {
		assertTrue(createdComponents);
	}

	@Test
	public void shouldSetBuiltPanelAsContentPane() {
		assertSame(PANEL, dialog.getContentPane());
	}

	@Test
	public void minimumSizeShouldBeSet() {
		assertTrue(dialog.isMinimumSizeSet());
		assertEquals(dialog.getMinimumSize(), dialog.getSize());
	}

	@Test
	public void shouldSetVisibleWhenNotTesting() {
		assertFalse(showed);
		dialog.setVisible(true);
		assertTrue(showed);
	}

	@Test
	public void shouldNotSetVisibleWhenTesting() {
		Whitebox.setInternalState(AbstractDialog.class, "suppressShow", true);
		dialog.setVisible(true);
		assertFalse(showed);
		assertFalse(Whitebox.getInternalState(AbstractDialog.class, boolean.class));
	}

	class DialogMock extends AbstractDialog {

		public DialogMock() {
			super(owner, TITLE);
		}

		@Override
		protected void createComponents(Component... innerComponents) {
			createdComponents = true;
		}

		@Override
		protected Container buildPanel() {
			return PANEL;
		}

		/**
		 * Overriding to mock show.
		 * 
		 * @deprecated Overriding to mock show.
		 */
		@Override
		@Deprecated
		@SuppressWarnings("deprecation")
		public void show() {
			showed = true;
		}
	}
}