package org.kalibro.desktop.swingextension.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.desktop.swingextension.table.Table;
import org.kalibro.tests.UnitTest;

public class TablePanelAdapterTest extends UnitTest {

	private TablePanelListener<Object> listener;
	private TablePanel<Object> panel;
	private Object element;

	private TablePanelAdapter<Object> adapter;

	@Before
	public void setUp() {
		listener = mock(TablePanelListener.class);
		element = mock(Object.class);
		mockPanel();
		adapter = new TablePanelAdapter<Object>(panel, listener);
	}

	private void mockPanel() {
		panel = mock(TablePanel.class);
		Table<Object> table = mock(Table.class);
		panel.table = table;
		when(table.getSelected()).thenReturn(element);
	}

	@Test
	public void shouldFireAdd() {
		fire("add");
		verify(listener).add();
	}

	@Test
	public void shouldFireEdit() {
		fire("edit");
		verify(listener).edit(element);
	}

	@Test
	public void shouldFireRemove() {
		fire("remove");
		verify(listener).remove(element);
	}

	private void fire(String action) {
		Component source = mock(Component.class);
		ActionEvent event = mock(ActionEvent.class);
		when(event.getSource()).thenReturn(source);
		when(source.getName()).thenReturn(action);
		adapter.actionPerformed(event);
	}
}