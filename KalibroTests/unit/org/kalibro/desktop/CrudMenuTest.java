package org.kalibro.desktop;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.desktop.swingextension.InternalFrame;
import org.mockito.Mockito;

public class CrudMenuTest extends KalibroTestCase {

	private static final int NEW = 0;
	private static final int OPEN = 1;
	private static final int DELETE = 2;
	private static final int SAVE = 4;
	private static final int SAVE_AS = 5;
	private static final int CLOSE = 6;

	private JDesktopPane desktopPane;
	private InternalFrame<BaseTool> frame;
	private CrudController<BaseTool> crudController;

	private CrudMenu<BaseTool> menu;

	@Before
	public void setUp() {
		frame = mock(InternalFrame.class);
		desktopPane = mock(JDesktopPane.class);
		crudController = mock(CrudController.class);
		menu = new BaseToolMenu(desktopPane);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNew() {
		checkItem(NEW, "new", "New", 'N');
		Mockito.verify(crudController).newEntity();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkOpen() {
		checkItem(OPEN, "open", "Open", 'O');
		Mockito.verify(crudController).open();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDelete() {
		checkItem(DELETE, "delete", "Delete", 'D');
		Mockito.verify(crudController).delete();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSave() {
		checkItem(SAVE, "save", "Save", 'S');
		Mockito.verify(crudController).save();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSaveAs() {
		checkItem(SAVE_AS, "saveAs", "Save as...", 'a');
		Mockito.verify(crudController).saveAs();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkClose() {
		checkItem(CLOSE, "close", "Close", 'l');
		Mockito.verify(crudController).close();
	}

	private void checkItem(int index, String name, String text, char mnemonic) {
		JMenuItem item = menu.getItem(index);
		assertEquals(name, item.getName());
		assertEquals(text, item.getText());
		assertEquals(mnemonic, item.getMnemonic());
		item.doClick();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void itemsForCurrentEntityShouldBeDisabledWhenThereIsNoEntityFrameSelected() {
		menu.menuSelected(null);
		assertFalse(menu.getItem(SAVE).isEnabled());
		assertFalse(menu.getItem(SAVE_AS).isEnabled());
		assertFalse(menu.getItem(CLOSE).isEnabled());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void itemsForCurrentEntityShouldBeEnabledWhenEntityFrameIsSelected() {
		when(desktopPane.getSelectedFrame()).thenReturn(frame);
		menu.menuSelected(null);
		assertTrue(menu.getItem(SAVE).isEnabled());
		assertTrue(menu.getItem(SAVE_AS).isEnabled());
		assertTrue(menu.getItem(CLOSE).isEnabled());
	}

	private class BaseToolMenu extends CrudMenu<BaseTool> {

		BaseToolMenu(JDesktopPane desktopPane) {
			super(desktopPane, "Base tool");
		}

		@Override
		protected void initializeController() {
			controller = crudController;
		}

		@Override
		protected boolean isEntityFrame(JInternalFrame selectedFrame) {
			return selectedFrame == frame;
		}
	}
}