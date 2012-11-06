package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class ReflectionTableModelTest extends UnitTest {

	private ReflectionTableModel<Reading> model;

	@Before
	public void setUp() {
		model = new ReflectionTableModel<Reading>(Reading.class);
		model.addColumn(new ReflectionColumn("label", 0));
		model.addColumn(new ReflectionColumn("grade", 0));
		model.addColumn(new ReflectionColumn("color", 0));
		model.setData(loadFixture("scholar", ReadingGroup.class).getReadings());
	}

	@Test
	public void shouldAcceptOnlyReflectionColumns() {
		assertThat(addGenericColumn()).throwsException()
			.withMessage("All columns of ReflectionTableModel should be ReflectionColumn");
	}

	private VoidTask addGenericColumn() {
		return new VoidTask() {

			@Override
			protected void perform() {
				model.addColumn(new Column("", null, 0));
			}
		};
	}

	@Test
	public void shouldGetColumnClasses() {
		assertEquals(String.class, model.getColumnClass(0));
		assertEquals(Double.class, model.getColumnClass(1));
		assertEquals(Color.class, model.getColumnClass(2));
	}

	@Test
	public void shouldGetValues() {
		assertEquals("Terrible", model.getValueAt(0, 0));
		assertDoubleEquals(5.0, (Double) model.getValueAt(4, 1));
		assertEquals(Color.GREEN, model.getValueAt(8, 2));
	}
}