package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.desktop.swingextension.icon.Icon;
import org.powermock.reflect.Whitebox;

public class AbstractDialogTest extends TestCase {

	private static final String TITLE = "AbstractDialogTest";
	private static final JPanel PANEL = new JPanel();

	private DialogMock dialog;
	private boolean createdComponents, showed;

	@Before
	public void setUp() {
		createdComponents = false;
		showed = false;
		dialog = new DialogMock();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetTitle() {
		assertSame(TITLE, dialog.getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldBeModal() {
		assertTrue(dialog.isModal());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveKalibroIcon() {
		assertDeepEquals(dialog.getIconImages(), new Icon(Icon.KALIBRO).getImage());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDisposeOnClose() {
		assertEquals(WindowConstants.DISPOSE_ON_CLOSE, dialog.getDefaultCloseOperation());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateComponents() {
		assertTrue(createdComponents);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetBuiltPanelAsContentPane() {
		assertSame(PANEL, dialog.getContentPane());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void minimumSizeShouldBeSet() {
		assertTrue(dialog.isMinimumSizeSet());
		assertEquals(dialog.getMinimumSize(), dialog.getSize());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetVisibleWhenNotTesting() {
		assertFalse(showed);
		dialog.setVisible(true);
		assertTrue(showed);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotSetVisibleWhenTesting() {
		Whitebox.setInternalState(AbstractDialog.class, "suppressShow", true);
		dialog.setVisible(true);
		assertFalse(showed);
		assertFalse(Whitebox.getInternalState(AbstractDialog.class, boolean.class));
	}

	class DialogMock extends AbstractDialog {

		public DialogMock() {
			super(TITLE);
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
		 * @deprecated Ble
		 */
		@Override
		@Deprecated
		@SuppressWarnings("deprecation")
		public void show() {
			showed = true;
		}
	}
}