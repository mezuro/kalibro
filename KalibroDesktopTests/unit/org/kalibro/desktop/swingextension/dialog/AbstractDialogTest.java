package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.Icon;
import org.kalibro.tests.UnitTest;
import org.powermock.reflect.Whitebox;

public class AbstractDialogTest extends UnitTest {

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

	@Test
	public void shouldSetTitle() {
		assertSame(TITLE, dialog.getTitle());
	}

	@Test
	public void shouldBeModal() {
		assertTrue(dialog.isModal());
	}

	@Test
	public void shouldHaveKalibroIcon() {
		assertDeepEquals(list(new Icon(Icon.KALIBRO).getImage()), dialog.getIconImages());
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