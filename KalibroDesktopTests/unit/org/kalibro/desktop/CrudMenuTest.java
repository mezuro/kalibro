package org.kalibro.desktop;

import static org.junit.Assert.*;

import javax.swing.JMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.core.Identifier;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({CrudMenu.class, KalibroFrame.class})
public class CrudMenuTest extends UnitTest {

	private CrudController<ReadingGroup> controller;

	private CrudMenu menu;

	@Before
	public void seUp() {
		controller = mock(CrudController.class);
		when(controller.getClassName()).thenReturn(Identifier.fromClassName("ReadingGroup"));
		menu = new CrudMenu(controller);
	}

	@Test
	public void checkMenuAttributes() {
		assertEquals("readingGroup", menu.getName());
		assertEquals("Reading group", menu.getText());
		assertEquals('R', menu.getMnemonic());
	}

	@Test
	public void shouldCreate() throws Exception {
		create().doClick();
		verify(controller).create();
	}

	@Test
	public void shouldOpen() {
		open().doClick();
		verify(controller).open();
	}

	@Test
	public void shouldDelete() {
		delete().doClick();
		verify(controller).delete();
	}

	@Test
	public void shouldSave() {
		save().doClick();
		verify(controller).save();
	}

	@Test
	public void shouldClose() {
		close().doClick();
		verify(controller).close();
	}

	@Test
	public void selectShouldEnableSaveAndCloseIfSelectedTabIsCompatible() {
		mockSelectedTitle("Some - Reading group");
		menu.menuSelected(null);
		assertTrue(save().isEnabled());
		assertTrue(close().isEnabled());
	}

	@Test
	public void selectShouldDisableSaveAndCloseIfSelectedTabIsNotCompatible() {
		mockSelectedTitle("Some - Configuration");
		menu.menuSelected(null);
		assertFalse(save().isEnabled());
		assertFalse(close().isEnabled());
	}

	private void mockSelectedTitle(String title) {
		KalibroFrame frame = mock(KalibroFrame.class);
		mockStatic(KalibroFrame.class);
		when(KalibroFrame.getInstance()).thenReturn(frame);
		when(frame.getSelectedTitle()).thenReturn(title);
	}

	private JMenuItem create() {
		return menu.getItem(0);
	}

	private JMenuItem open() {
		return menu.getItem(1);
	}

	private JMenuItem delete() {
		return menu.getItem(2);
	}

	private JMenuItem save() {
		return menu.getItem(4);
	}

	private JMenuItem close() {
		return menu.getItem(5);
	}
}