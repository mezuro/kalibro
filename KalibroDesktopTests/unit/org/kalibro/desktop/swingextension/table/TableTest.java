package org.kalibro.desktop.swingextension.table;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.List;
import java.util.Random;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(Table.class)
public class TableTest extends UnitTest {

	private static final int LINES = new Random().nextInt(20);
	private static final String NAME = "TableTest name";

	private Reading reading;
	private JTable innerTable;
	private TableModel<Reading> model;

	private Table<Reading> table;

	@Before
	public void setUp() throws Exception {
		reading = loadFixture("excellent", Reading.class);
		spyModel();
		table = new Table<Reading>(NAME, LINES, Reading.class);
		table.addColumn("label");
		table.add(reading);
		innerTable = new ComponentFinder(table).find(NAME, JTable.class);
		assertSame(model, innerTable.getModel());
	}

	private void spyModel() throws Exception {
		model = spy(new TableModel<Reading>(Reading.class));
		whenNew(TableModel.class).withArguments(Reading.class).thenReturn(model);
	}

	@Test
	public void shouldSetName() {
		assertEquals(NAME, table.getName());
		assertEquals(NAME, innerTable.getName());
	}

	@Test
	public void shouldShowGrid() {
		assertTrue(innerTable.getShowVerticalLines());
		assertTrue(innerTable.getShowHorizontalLines());
	}

	@Test
	public void shouldAutoCreateRowSorter() {
		assertTrue(innerTable.getAutoCreateRowSorter());
	}

	@Test
	public void shouldNotAllowColumnSelection() {
		assertFalse(innerTable.getColumnSelectionAllowed());
	}

	@Test
	public void shouldNotAutoResize() {
		assertEquals(JTable.AUTO_RESIZE_OFF, innerTable.getAutoResizeMode());
	}

	@Test
	public void shouldNotAllowMultipleRowsSelection() {
		assertEquals(ListSelectionModel.SINGLE_SELECTION, innerTable.getSelectionModel().getSelectionMode());
	}

	@Test
	public void shouldAddColumnToModel() {
		verify(model).addColumn("label");
	}

	@Test
	public void shouldUpdateModelOnPack() {
		table.pack();

		verify(model).fireTableStructureChanged();
		verify(model).updateColumnModel(innerTable.getColumnModel());
	}

	@Test
	public void shouldAdjustSizeOnPack() {
		table.pack();

		assertTrue(table.isMinimumSizeSet());
		assertTrue(table.isPreferredSizeSet());
		verifySize(table.getMinimumSize());
		verifySize(table.getPreferredSize());
	}

	private void verifySize(Dimension size) {
		assertEquals(model.getWidth(), size.width);
		assertEquals(innerTable.getRowHeight() * (LINES + 2), size.height);
	}

	@Test
	public void shouldGetDataFromModel() {
		assertDeepEquals(model.getData(), table.get());
	}

	@Test
	public void shouldSetDataOnModel() {
		List<Reading> data = list(reading);
		table.set(data);
		verify(model).setData(data);
	}

	@Test
	public void shouldNotHaveSelectionByDefault() {
		assertFalse(table.hasSelection());
	}

	@Test
	public void shouldGetSelectedObject() {
		innerTable.getSelectionModel().setSelectionInterval(0, 0);
		assertTrue(table.hasSelection());
		assertSame(model.getElementAt(0), table.getSelected());
	}

	@Test
	public void shouldAddElementToModel() {
		verify(model).add(reading);
	}

	@Test
	public void shouldReplaceElementOnModel() {
		Reading newReading = new Reading();
		table.replace(reading, newReading);
		verify(model).replace(reading, newReading);
	}

	@Test
	public void shouldRemoveElementFromModel() {
		table.remove(reading);
		verify(model).remove(reading);
	}

	@Test
	public void shouldAddTableListener() throws Exception {
		TableAdapter<Reading> adapter = mock(TableAdapter.class);
		TableListener<Reading> listener = mock(TableListener.class);
		whenNew(TableAdapter.class).withArguments(table, listener).thenReturn(adapter);

		table.addTableListener(listener);
		assertTrue(list(innerTable.getMouseListeners()).contains(adapter));
		DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) innerTable.getSelectionModel();
		assertTrue(list(selectionModel.getListSelectionListeners()).contains(adapter));
	}

	@Test
	public void shouldSetEnabledOnInnerTable() {
		assertTrue(table.isEnabled());
		assertTrue(innerTable.isEnabled());

		table.setEnabled(false);
		assertFalse(table.isEnabled());
		assertFalse(innerTable.isEnabled());
	}
}