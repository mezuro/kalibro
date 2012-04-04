package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.desktop.swingextension.Button;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class TablePanelTest extends KalibroTestCase {

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
		table.setData(metricConfiguration("amloc").getRanges());
		table = PowerMockito.spy(table);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testShow() {
		panel.set(new ArrayList<Range>());
		assertTrue(table().getData().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRetrieve() {
		assertSame(table().getData(), panel.get());
	}

	private Table<Range> table() {
		return finder.find("ranges", Table.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
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

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddButton() {
		button("add").doClick();
		Mockito.verify(listener).add();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testEditButton() {
		selectFirstLine();
		button("edit").doClick();
		Mockito.verify(listener).edit(table.getSelected());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveButton() {
		selectFirstLine();
		Range selected = table.getSelected();
		button("remove").doClick();
		Mockito.verify(table).remove(selected);
	}

	private Button button(String name) {
		return finder.find(name, Button.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
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