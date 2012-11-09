package org.kalibro.desktop.swingextension.table;

import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class ColumnRendererTest extends UnitTest {

	private Component component, renderedComponent;
	private Object value, context;
	private boolean isSelected;

	private ColumnRenderer renderer;

	@Before
	public void setUp() throws Exception {
		value = mock(Object.class);
		context = mock(Object.class);
		component = mock(Component.class);
		renderer = mockAbstract(ColumnRenderer.class);
		doReturn(component).when(renderer).render(any(), any());
		render();
	}

	private void render() {
		Random random = new Random();
		isSelected = random.nextBoolean();
		boolean hasFocus = random.nextBoolean();
		int column = random.nextInt(), view = random.nextInt(), row = random.nextInt();
		Color background = new Color(random.nextInt());
		when(component.getBackground()).thenReturn(background);

		JTable table = mock(JTable.class);
		TableModel<Object> model = mock(TableModel.class);
		when(table.getModel()).thenReturn(model);
		when(table.convertRowIndexToModel(view)).thenReturn(row);
		when(model.getElementAt(row)).thenReturn(context);
		renderedComponent = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, view, column);
	}

	@Test
	public void shouldRenderValueWithContext() {
		verify(renderer).render(value, context);
	}

	@Test
	public void shouldSetSelectionBackground() throws Exception {
		verifyPrivate(renderer).invoke("setSelectionBackground", component, isSelected);
	}

	@Test
	public void shouldReturnRenderedComponent() {
		assertSame(component, renderedComponent);
	}
}