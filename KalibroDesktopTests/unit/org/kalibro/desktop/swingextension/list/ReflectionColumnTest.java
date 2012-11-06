package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;
import org.kalibro.tests.UnitTest;
import org.powermock.reflect.Whitebox;

public class ReflectionColumnTest extends UnitTest {

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
		assertEquals(int.class, column.getColumnClass(Reading.class));
	}

	@Test
	public void checkValue() {
		Reading reading = loadFixture("excellent", Reading.class);
		assertEquals(0, column.getValue(reading));
	}

	@Test
	public void checkErrorGettingMethodFromInvalidClass() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				column.getColumnClass(String.class);
			}
		}).throwsError().withMessage("Reflection column did not found method: java.lang.String.getColor")
			.withCause(NoSuchMethodException.class);
	}

	@Test
	public void shouldReturnNullInCaseOfInvokationError() {
		assertNull(column.getValue(new Reading() {

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