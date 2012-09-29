package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;
import static org.kalibro.MetricConfigurationFixtures.metricConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Range;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(Table.class)
public class TableTest extends UnitTest {

	private List<Range> data;
	private TableModel<Range> model;

	private JTable innerTable;
	private Table<Range> table;

	@Before
	public void setUp() {
		data = new ArrayList<Range>(metricConfiguration("amloc").getRanges());
		createModel();
		table = new Table<Range>("ranges", model, 5);
		table.setData(data);
		innerTable = new ComponentFinder(table).find("ranges", JTable.class);
	}

	private void createModel() {
		model = new MyModel();
		model.addColumn(new Column("Label", String.class, 42));
		model = spy(model);
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
	public void shouldUpdateColumnModel() {
		verify(model).updateColumnModel(innerTable.getColumnModel());
	}

	@Test
	public void shouldGetDataFromModel() {
		when(model.getData()).thenReturn(null);
		assertNull(table.getData());
	}

	@Test
	public void shouldSetDataOnModel() {
		verify(model).setData(data);
	}

	@Test
	public void shouldRemoveRowFromModel() {
		Range range = data.get(0);
		table.remove(range);
		verify(model).remove(range);
	}

	@Test
	public void shouldNotHaveSelectionByDefault() {
		assertFalse(table.hasSelection());
	}

	@Test
	public void checkSelectedObject() {
		innerTable.getSelectionModel().setSelectionInterval(0, 0);
		assertTrue(table.hasSelection());
		assertSame(data.get(0), table.getSelected());
	}

	@Test
	public void testEnabled() {
		assertTrue(innerTable.isEnabled());
		table.setEnabled(false);
		assertFalse(table.isEnabled());
		assertFalse(innerTable.isEnabled());
	}

	@Test
	public void shouldAddListListener() throws Exception {
		ListListener<Range> listener = mock(ListListener.class);
		ListComponentAdapter<Range> adapter = mock(ListComponentAdapter.class);
		whenNew(ListComponentAdapter.class).withArguments(listener, table).thenReturn(adapter);

		table.addListListener(listener);
		assertTrue(asList(innerTable.getMouseListeners()).contains(adapter));
		DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) innerTable.getSelectionModel();
		assertTrue(asList(selectionModel.getListSelectionListeners()).contains(adapter));
	}

	private class MyModel extends TableModel<Range> {

		@Override
		public String getValueAt(int row, int column) {
			return getObjectAt(row).getLabel();
		}
	}
}