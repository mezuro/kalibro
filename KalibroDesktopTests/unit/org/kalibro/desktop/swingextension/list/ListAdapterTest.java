package org.kalibro.desktop.swingextension.list;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

public class ListAdapterTest extends UnitTest {

	private ListAdapter<String> adapter;

	@Before
	public void setUp() {
		adapter = PowerMockito.spy(new MyListAdapter());
	}

	@Test
	public void doubleClickedShouldDoNothing() {
		adapter.doubleClicked("");
		Mockito.verify(adapter).doubleClicked("");
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void selectedShouldDoNothing() {
		adapter.selected("");
		Mockito.verify(adapter).selected("");
		Mockito.verifyNoMoreInteractions(adapter);
	}

	@Test
	public void selectionClearedShouldDoNothing() {
		adapter.selectionCleared();
		Mockito.verify(adapter).selectionCleared();
		Mockito.verifyNoMoreInteractions(adapter);
	}

	private class MyListAdapter extends ListAdapter<String> {

		protected MyListAdapter() {
			super();
		}
	}
}