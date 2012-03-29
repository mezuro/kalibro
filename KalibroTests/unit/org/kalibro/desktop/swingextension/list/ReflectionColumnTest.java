package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;
import org.powermock.reflect.Whitebox;

public class ReflectionColumnTest extends KalibroTestCase {

	private ReflectionColumn column;

	@Before
	public void setUp() {
		column = new ReflectionColumn("color.red", 42, new ColorRenderer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetDefaultRenderer() {
		column = new ReflectionColumn("", 0);
		assertClassEquals(DefaultRenderer.class, Whitebox.getInternalState(column, "renderer"));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTitle() {
		assertEquals("Red", column.getTitle());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkColumnClass() {
		assertEquals(int.class, column.getColumnClass(Range.class));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkValue() {
		Range greenRange = RangeFixtures.amlocRange(RangeLabel.EXCELLENT);
		assertEquals(0, column.getValue(greenRange));

		Range redRange = RangeFixtures.amlocRange(RangeLabel.BAD);
		assertEquals(255, column.getValue(redRange));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorGettingMethodFromInvalidClass() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				column.getColumnClass(String.class);
			}
		}, "Reflection column did not found method: java.lang.String.getColor", NoSuchMethodException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
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