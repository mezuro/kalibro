package org.kalibro.desktop.swingextension.table;

import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class TableAdapterTest extends UnitTest {

	private TableListener<Object> listener;
	private Table<Object> table;
	private Object element;

	private TableAdapter<Object> adapter;

	@Before
	public void setUp() {
		listener = mock(TableListener.class);
		element = mock(Object.class);
		table = mock(Table.class);
		when(table.getSelected()).thenReturn(element);

		adapter = new TableAdapter<Object>(listener, table);
	}

	@Test
	public void shouldNotFireSingleClick() {
		mouseClicked(1);
		verify(listener, never()).doubleClicked(element);
	}

	@Test
	public void shouldFireDoubleClick() {
		mouseClicked(2);
		verify(listener).doubleClicked(element);
	}

	private void mouseClicked(int clickCount) {
		MouseEvent event = mock(MouseEvent.class);
		when(event.getClickCount()).thenReturn(clickCount);
		adapter.mouseClicked(event);
	}

	@Test
	public void shouldFireSelection() {
		valueChanges(true);
		verify(listener).selected(element);
	}

	@Test
	public void shouldFireSelectionCleared() {
		valueChanges(false);
		verify(listener).selectionCleared();
	}

	private void valueChanges(boolean hasSelection) {
		ListSelectionEvent event = mock(ListSelectionEvent.class);
		when(event.getValueIsAdjusting()).thenReturn(false);
		when(table.hasSelection()).thenReturn(hasSelection);
		adapter.valueChanged(event);
	}

	@Test
	public void shouldNotFireIfValueIsAdjusting() {
		ListSelectionEvent event = mock(ListSelectionEvent.class);
		when(event.getValueIsAdjusting()).thenReturn(true);

		adapter.valueChanged(event);
		verifyZeroInteractions(listener, table);
	}
}