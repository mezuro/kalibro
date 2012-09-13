package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Range;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;
import org.powermock.reflect.Whitebox;

public class ReflectionColumnTest extends TestCase {

	private ReflectionColumn column;

	@Before
	public void setUp() {
		column = new ReflectionColumn("color.red", 42, new ColorRenderer());
	}

	@Test
	public void shouldSetDefaultRenderer() {
		column = new ReflectionColumn("", 0);
		assertClassEquals(DefaultRenderer.class, Whitebox.getInternalState(column, "renderer"));
	}

	@Test
	public void checkTitle() {
		assertEquals("Red", column.getTitle());
	}

	@Test
	public void checkColumnClass() {
		assertEquals(int.class, column.getColumnClass(Range.class));
	}

	@Test
	public void checkValue() {
		Range greenRange = newRange("amloc", EXCELLENT);
		assertEquals(0, column.getValue(greenRange));

		Range redRange = newRange("amloc", BAD);
		assertEquals(255, column.getValue(redRange));
	}

	@Test
	public void checkErrorGettingMethodFromInvalidClass() {
		checkKalibroError(new Task() {

			@Override
			public void perform() throws Exception {
				column.getColumnClass(String.class);
			}
		}, "Reflection column did not found method: java.lang.String.getColor", NoSuchMethodException.class);
	}

	@Test
	public void shouldReturnNullInCaseOfInvokationError() {
		assertNull(column.getValue(new Range() {

			@Override
			public Color getColor() {
				throw new RuntimeException();
			}
		}));
	}

	private class ColorRenderer extends TableRenderer {

		@Override
		protected JPanel render(Object value, Object context) {
			JPanel panel = new JPanel();
			panel.setBackground((Color) value);
			return panel;
		}
	}
}