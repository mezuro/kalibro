package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;

public class TableModelTest extends KalibroTestCase {

	private List<Range> data;
	private TableModel<Range> model;

	@Before
	public void setUp() {
		data = PowerMockito.spy(new ArrayList<Range>(configuration("amloc").getRanges()));
		model = PowerMockito.spy(new MyModel());
		model.setData(data);
		model.addColumn(new Column("Label", String.class, 42));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkData() {
		assertDeepEquals(data, model.getData());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyDataChanged() {
		model.setData(Arrays.asList(new Range()));
		verify(model, atLeastOnce()).fireTableDataChanged();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkObjectByRow() {
		for (int i = 0; i < data.size(); i++)
			assertSame(data.get(i), model.getObjectAt(i));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveRow() {
		Range range = data.get(0);
		model.remove(range);
		assertFalse(model.getData().contains(range));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyRemoval() {
		model.remove(data.get(0));
		verify(model, atLeastOnce()).fireTableDataChanged();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveColumnCount() {
		assertEquals(1, model.getColumnCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveColumnName() {
		assertEquals("Label", model.getColumnName(0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveColumnClass() {
		assertEquals(String.class, model.getColumnClass(0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrievePreferredWidth() {
		assertEquals(10 + model.columns.get(0).getPreferredWidth(), model.getPreferredWidth());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUpdateColumnModel() {
		TableColumn tableColumn = PowerMockito.mock(TableColumn.class);
		TableColumnModel columnModel = PowerMockito.mock(TableColumnModel.class);
		PowerMockito.when(columnModel.getColumnCount()).thenReturn(1);
		PowerMockito.when(columnModel.getColumn(0)).thenReturn(tableColumn);

		model.updateColumnModel(columnModel);
		verify(tableColumn).setPreferredWidth(anyInt());
		ArgumentCaptor<TableCellRenderer> captor = ArgumentCaptor.forClass(TableCellRenderer.class);
		verify(tableColumn).setCellRenderer(captor.capture());
		assertClassEquals(DefaultRenderer.class, captor.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void rowCountShouldBeDataSize() {
		assertEquals(data.size(), model.getRowCount());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddColumn() {
		model.addColumn(new Column("Comments", String.class, 84));
		assertEquals(2, model.getColumnCount());
	}

	private class MyModel extends TableModel<Range> {

		@Override
		public String getValueAt(int row, int column) {
			return getObjectAt(row).getLabel();
		}
	}
}