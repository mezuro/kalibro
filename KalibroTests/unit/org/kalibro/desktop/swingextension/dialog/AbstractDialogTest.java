package org.kalibro.desktop.swingextension.dialog;

import static org.junit.Assert.*;

import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.swingextension.icon.Icon;

public class AbstractDialogTest extends KalibroTestCase {

	private static final String TITLE = "AbstractDialogTest";
	private static final JPanel PANEL = new JPanel();

	private DialogStub dialog;
	private boolean createdComponents;

	@Before
	public void setUp() {
		createdComponents = false;
		dialog = new DialogStub();
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

	class DialogStub extends AbstractDialog {

		public DialogStub() {
			super(TITLE);
		}

		@Override
		protected void createComponents() {
			createdComponents = true;
		}

		@Override
		protected Container buildPanel() {
			return PANEL;
		}
	}
}