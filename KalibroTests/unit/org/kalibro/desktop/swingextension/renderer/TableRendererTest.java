package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.desktop.swingextension.list.TableModel;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class TableRendererTest extends KalibroTestCase {

	private boolean isSelected;
	private Component component;
	private TableRenderer renderer;

	@Before
	public void setUp() {
		component = PowerMockito.mock(Component.class);
		renderer = PowerMockito.spy(new MyRenderer());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderValueWithContext() {
		render();
		Mockito.verify(renderer).render("value", "context");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldChangeBackgroundIfSelected() {
		render();
		Mockito.verify(renderer).changeBackgroundIfSelected(component, isSelected);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnRenderedComponent() {
		assertSame(component, render());
	}

	private Component render() {
		Random random = new Random(System.currentTimeMillis());
		isSelected = random.nextBoolean();
		boolean hasFocus = random.nextBoolean();
		int column = random.nextInt();
		Color background = new Color(random.nextInt());
		PowerMockito.when(component.getBackground()).thenReturn(background);

		JTable table = PowerMockito.mock(JTable.class);
		TableModel<String> model = PowerMockito.mock(TableModel.class);
		PowerMockito.when(table.getModel()).thenReturn(model);
		PowerMockito.when(table.convertRowIndexToModel(24)).thenReturn(42);
		PowerMockito.when(model.getObjectAt(42)).thenReturn("context");
		return renderer.getTableCellRendererComponent(table, "value", isSelected, hasFocus, 24, column);
	}

	private class MyRenderer extends TableRenderer {

		@Override
		protected Component render(Object value, Object context) {
			return component;
		}
	}
}