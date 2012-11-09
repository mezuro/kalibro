package org.kalibro.desktop.swingextension.table;

import static org.junit.Assert.*;

import java.util.Random;

import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Range;
import org.kalibro.desktop.swingextension.field.StringField;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentCaptor;
import org.powermock.reflect.Whitebox;

public class ColumnTest extends UnitTest {

	private TableColumn tableColumn;

	private Column column;

	@Before
	public void setUp() {
		tableColumn = mock(TableColumn.class);
		column = new Column(Range.class, "reading", "label");
	}

	@Test
	public void shouldHaveDefaultColumnRenderer() {
		assertClassEquals(DefaultRenderer.class, captureRenderer());
	}

	@Test
	public void shouldSetCustomRenderer() {
		ColumnRenderer customRenderer = mock(ColumnRenderer.class);
		column.renderedBy(customRenderer);
		assertSame(customRenderer, captureRenderer());
	}

	private ColumnRenderer captureRenderer() {
		column.update(tableColumn);
		ArgumentCaptor<ColumnRenderer> captor = ArgumentCaptor.forClass(ColumnRenderer.class);
		verify(tableColumn).setCellRenderer(captor.capture());
		return captor.getValue();
	}

	@Test
	public void shouldHaveDefaultColumnWidth() {
		int defaultColumnWidth = Whitebox.getInternalState(Column.class, "DEFAULT_WIDTH");
		assertEquals(defaultColumnWidth, column.getWidth());
	}

	@Test
	public void shouldSetCustomColumnWidth() {
		int customCharsWidth = Math.abs(new Random().nextInt());
		int customWidth = new StringField("", customCharsWidth).getPreferredSize().width;
		assertEquals(customWidth, column.withWidth(customCharsWidth).getWidth());
	}

	@Test
	public void shouldGetDefaultTitleFromFieldPath() {
		assertEquals("Reading label", column.getTitle());
	}

	@Test
	public void shouldSetCustomTitle() {
		String customTitle = "Label";
		assertEquals(customTitle, column.titled(customTitle).getTitle());
	}

	@Test
	public void shouldGetColumnClass() {
		assertEquals(String.class, column.getColumnClass());
	}

	@Test
	public void shouldGetValue() {
		Range range = loadFixture("lcom4-bad", Range.class);
		assertEquals(range.getReading().getLabel(), column.getValue(range));
	}

	@Test
	public void shouldUpdateTableColumnWidth() {
		column.update(tableColumn);
		verify(tableColumn).setPreferredWidth(column.getWidth());
	}
}