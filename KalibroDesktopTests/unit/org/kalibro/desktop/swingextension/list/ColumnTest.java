package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.assertEquals;

import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

public class ColumnTest extends TestCase {

	private Column column;

	@Before
	public void setUp() {
		column = new Column("Answer", Double.class, 42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetDefaultRenderer() {
		assertClassEquals(DefaultRenderer.class, renderer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTitle() {
		assertEquals("Answer", column.getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkColumnClass() {
		assertEquals(Double.class, column.getColumnClass());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkWidth() {
		int expected = new StringField("", 42).getPreferredSize().width;
		assertEquals(expected, column.getPreferredWidth());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUpdateTableColumn() {
		TableColumn tableColumn = PowerMockito.mock(TableColumn.class);
		column.updateTableColumn(tableColumn);
		Mockito.verify(tableColumn).setPreferredWidth(column.getPreferredWidth());
		Mockito.verify(tableColumn).setCellRenderer(renderer());
	}

	private TableRenderer renderer() {
		return (TableRenderer) Whitebox.getInternalState(column, "renderer");
	}
}