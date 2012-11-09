package org.kalibro.desktop.swingextension.table;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

public class TableModelTest extends UnitTest {

	private TableModel<Reading> model;

	@Before
	public void setUp() {
		model = spy(new TableModel<Reading>(Reading.class));
		model.addColumn("label");
		model.addColumn("grade");
		model.addColumn("color");
		model.setData(loadFixture("scholar", ReadingGroup.class).getReadings());
		Mockito.reset(model);
	}

	@Test
	public void shouldSafeCopyData() {
		List<Reading> data = list();
		model.setData(data);
		assertNotSame(data, model.getData());
		assertDeepEquals(data, model.getData());
	}

	@Test
	public void shouldFireDataChangeOnSetData() {
		model.setData(new ArrayList<Reading>());
		verify(model).fireTableDataChanged();
	}

	@Test
	public void shouldGetElementAtRow() {
		List<Reading> data = model.getData();
		for (Reading reading : data)
			assertSame(reading, model.getElementAt(data.indexOf(reading)));
	}

	@Test
	public void shouldAddElement() {
		Reading reading = new Reading();
		model.add(reading);
		assertSame(reading, model.getElementAt(model.getRowCount() - 1));
	}

	@Test
	public void shouldFireDataChangeOnAddElement() {
		model.add(new Reading());
		verify(model).fireTableDataChanged();
	}

	@Test
	public void shouldReplaceElement() {
		Reading oldReading = model.getElementAt(0);
		Reading newReading = new Reading();
		model.replace(oldReading, newReading);
		assertFalse(model.getData().contains(oldReading));
		assertSame(newReading, model.getElementAt(0));
	}

	@Test
	public void shouldFireDataChangeOnReplaceElement() {
		model.replace(model.getElementAt(0), new Reading());
		verify(model).fireTableDataChanged();
	}

	@Test
	public void shouldRemoveElement() {
		Reading reading = model.getElementAt(0);
		model.remove(reading);
		assertFalse(model.getData().contains(reading));
	}

	@Test
	public void shouldFireDataChangeOnRemoveElement() {
		model.remove(model.getElementAt(0));
		verify(model).fireTableDataChanged();
	}

	@Test
	public void shouldGetWidth() {
		int scrollMargin = Whitebox.getInternalState(TableModel.class, "SCROLL_MARGIN");
		int columnWidth = Whitebox.getInternalState(Column.class, "DEFAULT_WIDTH");
		int expectedWidth = scrollMargin + model.getColumnCount() * columnWidth;
		assertEquals(expectedWidth, model.getWidth());
	}

	@Test
	public void shouldGetRowCount() {
		assertEquals(model.getData().size(), model.getRowCount());
	}

	@Test
	public void shouldGetColumnCount() {
		assertEquals(3, model.getColumnCount());
	}

	@Test
	public void shouldGetColumnName() {
		assertEquals("Label", model.getColumnName(0));
		assertEquals("Grade", model.getColumnName(1));
		assertEquals("Color", model.getColumnName(2));
	}

	@Test
	public void shouldGetColumnClass() {
		assertEquals(String.class, model.getColumnClass(0));
		assertEquals(Double.class, model.getColumnClass(1));
		assertEquals(Color.class, model.getColumnClass(2));
	}

	@Test
	public void shouldGetValueAtPosition() {
		assertEquals("Terrible", model.getValueAt(0, 0));
		assertDoubleEquals(5.0, (Double) model.getValueAt(4, 1));
		assertEquals(Color.GREEN, model.getValueAt(8, 2));
	}

	@Test
	public void shouldUpdateTableColumnModel() {
		TableColumn tableColumn = mock(TableColumn.class);
		TableColumnModel columnModel = mock(TableColumnModel.class);
		when(columnModel.getColumnCount()).thenReturn(1);
		when(columnModel.getColumn(0)).thenReturn(tableColumn);

		model.updateColumnModel(columnModel);
		verify(tableColumn).setPreferredWidth(anyInt());
		verify(tableColumn).setCellRenderer(any(TableCellRenderer.class));
	}
}