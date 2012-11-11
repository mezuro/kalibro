package org.kalibro.desktop.swingextension.panel;

import static org.junit.Assert.assertEquals;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;

public class TablePanelTest extends UnitTest {

	private static final String NAME = "TablePanelTest name";

	private TablePanelListener<Reading> listener;
	private Table<Reading> table;
	private Reading reading;

	private TablePanel<Reading> panel;
	private ComponentFinder finder;

	@Before
	public void setUp() {
		listener = mock(TablePanelListener.class);
		reading = loadFixture("excellent", Reading.class);
		createTable();
		panel = new TablePanel<Reading>(table);
		panel.addTablePanelListener(listener);
		finder = new ComponentFinder(panel);
	}

	private void createTable() {
		table = new Table<Reading>(NAME, 9, Reading.class);
		table.addColumn("label").withWidth(15);
		table.addColumn("grade").withWidth(8);
		table.pack();
		table.add(reading);
		table = spy(table);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, panel.getName());
	}

	@Test
	public void shouldGetFromTable() {
		assertDeepEquals(table.get(), panel.get());
	}

	@Test
	public void shouldSetOnTable() {
		List<Reading> data = list();
		panel.set(data);
		verify(table).set(data);
	}

	@Test
	public void onlyAddButtonShouldBeEnabledByDefault() {
		assertEnabled(true, false, false);
	}

	@Test
	public void selectShoudShouldEnabledEditAndRemove() {
		selectReading();
		assertEnabled(true, true, true);
	}

	@Test
	public void selectionClearedShouldDisableEditAndRemove() {
		innerTable().clearSelection();
		assertEnabled(true, false, false);
	}

	private void assertEnabled(boolean addEnabled, boolean editEnabled, boolean removeEnabled) {
		assertEquals(addEnabled, button("add").isEnabled());
		assertEquals(editEnabled, button("edit").isEnabled());
		assertEquals(removeEnabled, button("remove").isEnabled());
	}

	@Test
	public void shouldAdd() {
		button("add").doClick();
		verify(listener).add();
	}

	@Test
	public void shouldEdit() {
		selectReading();
		button("edit").doClick();
		verify(listener).edit(reading);
	}

	@Test
	public void shouldRemove() {
		selectReading();
		button("remove").doClick();
		verify(listener).remove(reading);
		verify(table).remove(reading);
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}

	@Test
	public void shouldEditOnDoubleClick() {
		doubleClickFirstRow();
		verify(listener).edit(reading);
	}

	private void doubleClickFirstRow() {
		JTable innerTable = innerTable();
		selectReading();
		for (MouseListener mouseListener : innerTable.getMouseListeners())
			mouseListener.mouseClicked(new MouseEvent(innerTable, 0, 0, 0, 0, 0, 0, 0, 2, false, 0));
	}

	private void selectReading() {
		innerTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	private JTable innerTable() {
		return finder.find(NAME, JTable.class);
	}
}