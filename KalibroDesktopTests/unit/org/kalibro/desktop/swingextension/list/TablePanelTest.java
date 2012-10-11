package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class TablePanelTest extends UnitTest {

	private Table<Range> table;
	private TablePanelListener<Range> listener;

	private ComponentFinder finder;
	private TablePanel<Range> panel;

	@Before
	public void setUp() {
		createTable();
		listener = PowerMockito.mock(TablePanelListener.class);
		panel = new TablePanel<Range>(table);
		panel.addTablePanelListener(listener);
		finder = new ComponentFinder(panel);
	}

	private void createTable() {
		TableModel<Range> model = new ReflectionTableModel<Range>(Range.class);
		model.addColumn(new ReflectionColumn("label", 15));
		table = new Table<Range>("ranges", model, 5);
		table.setData(asList(loadFixture("lcom4-bad", Range.class)));
		table = PowerMockito.spy(table);
	}

	@Test
	public void testShow() {
		panel.set(new ArrayList<Range>());
		assertTrue(table().getData().isEmpty());
	}

	@Test
	public void testRetrieve() {
		assertSame(table().getData(), panel.get());
	}

	private Table<Range> table() {
		return finder.find("ranges", Table.class);
	}

	@Test
	public void testEnableButtons() {
		assertEnabled(true, false, false);

		selectFirstLine();
		assertEnabled(true, true, true);

		innerTable().clearSelection();
		assertEnabled(true, false, false);
	}

	private void assertEnabled(boolean addEnabled, boolean editEnabled, boolean removeEnabled) {
		assertEquals(addEnabled, button("add").isEnabled());
		assertEquals(editEnabled, button("edit").isEnabled());
		assertEquals(removeEnabled, button("remove").isEnabled());
	}

	@Test
	public void testAddButton() {
		button("add").doClick();
		Mockito.verify(listener).add();
	}

	@Test
	public void testEditButton() {
		selectFirstLine();
		button("edit").doClick();
		Mockito.verify(listener).edit(table.getSelected());
	}

	@Test
	public void testRemoveButton() {
		selectFirstLine();
		Range selected = table.getSelected();
		button("remove").doClick();
		Mockito.verify(table).remove(selected);
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}

	@Test
	public void testDoubleClick() {
		doubleClickFirstRow();
		Mockito.verify(listener).edit(table.getSelected());
	}

	private void doubleClickFirstRow() {
		JTable innerTable = innerTable();
		innerTable.getSelectionModel().setSelectionInterval(1, 1);
		for (MouseListener mouseListener : innerTable.getMouseListeners())
			mouseListener.mouseClicked(new MouseEvent(innerTable, 0, 0, 0, 0, 0, 0, 0, 2, false, 0));
	}

	private void selectFirstLine() {
		innerTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	private JTable innerTable() {
		return finder.find("ranges", JTable.class);
	}
}