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

	private String title;
	private JPanel panel;
	private boolean createdComponents;

	private DialogMock dialog;

	@Before
	public void setUp() {
		title = "My title";
		panel = new JPanel();
		createdComponents = false;
		dialog = new DialogMock();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetTitle() {
		assertSame(title, dialog.getTitle());
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
		assertSame(panel, dialog.getContentPane());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void minimumSizeShouldBeSet() {
		assertTrue(dialog.isMinimumSizeSet());
		assertEquals(dialog.getMinimumSize(), dialog.getSize());
	}

	class DialogMock extends AbstractDialog {

		public DialogMock() {
			super(title);
		}

		@Override
		protected void createComponents() {
			createdComponents = true;
		}

		@Override
		protected Container buildPanel() {
			return panel;
		}
	}
}