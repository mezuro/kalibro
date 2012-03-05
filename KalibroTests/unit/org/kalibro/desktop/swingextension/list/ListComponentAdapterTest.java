package org.kalibro.desktop.swingextension.list;

import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Range;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ListComponentAdapterTest extends KalibroTestCase {

	private Range range;
	private ListListener<Range> listener;
	private ListComponent<Range> component;

	private ListComponentAdapter<Range> adapter;

	@Before
	public void setUp() {
		mockComponent();
		listener = PowerMockito.mock(ListListener.class);
		adapter = new ListComponentAdapter<Range>(listener, component);
	}

	private void mockComponent() {
		range = new Range(0.0, Double.POSITIVE_INFINITY);
		component = PowerMockito.mock(ListComponent.class);
		PowerMockito.when(component.getSelected()).thenReturn(range);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyDoubleClick() {
		MouseEvent event = PowerMockito.mock(MouseEvent.class);
		PowerMockito.when(event.getClickCount()).thenReturn(2);

		adapter.mouseClicked(event);
		Mockito.verify(listener).doubleClicked(range);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifySelected() {
		ListSelectionEvent event = PowerMockito.mock(ListSelectionEvent.class);
		PowerMockito.when(component.hasSelection()).thenReturn(true);

		adapter.valueChanged(event);
		Mockito.verify(listener).selected(range);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifySelectionCleared() {
		adapter.valueChanged(PowerMockito.mock(ListSelectionEvent.class));
		Mockito.verify(listener).selectionCleared();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotNotifyIfValueIsAdjusting() {
		ListSelectionEvent event = PowerMockito.mock(ListSelectionEvent.class);
		PowerMockito.when(event.getValueIsAdjusting()).thenReturn(true);

		adapter.valueChanged(event);
		Mockito.verifyZeroInteractions(listener, component);
	}
}