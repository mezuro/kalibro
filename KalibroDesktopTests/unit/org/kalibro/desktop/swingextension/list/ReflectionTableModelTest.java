package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.MetricConfigurationFixtures.metricConfiguration;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Range;

public class ReflectionTableModelTest extends TestCase {

	private ReflectionTableModel<Range> model;

	@Before
	public void setUp() {
		model = new ReflectionTableModel<Range>(Range.class);
		model.addColumn(new ReflectionColumn("beginning", 0));
		model.addColumn(new ReflectionColumn("label", 0));
		model.addColumn(new ReflectionColumn("color", 0));
		model.setData(metricConfiguration("amloc").getRanges());
	}

	@Test
	public void shouldAcceptOnlyReflectionColumns() {
		assertThat(addGenericColumn()).throwsException()
			.withMessage("All columns of ReflectionTableModel should be ReflectionColumn");
	}

	private Task addGenericColumn() {
		return new Task() {

			@Override
			public void perform() throws Exception {
				model.addColumn(new Column("", null, 0));
			}
		};
	}

	@Test
	public void shouldGetColumnClasses() {
		assertEquals(Double.class, model.getColumnClass(0));
		assertEquals(String.class, model.getColumnClass(1));
		assertEquals(Color.class, model.getColumnClass(2));
	}

	@Test
	public void shouldGetValues() {
		assertDoubleEquals(0.0, (Double) model.getValueAt(0, 0));
		assertEquals("Good", model.getValueAt(1, 1));
		assertEquals(Color.YELLOW, model.getValueAt(2, 2));
	}
}