package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;

import java.util.SortedSet;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Range;
import org.kalibro.desktop.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(List.class)
public class ListTest extends UnitTest {

	private SortedSet<Range> ranges;

	private List<Range> list;
	private JList innerList;

	@Before
	public void setUp() {
		ranges = asSortedSet(loadFixture("lcom4-bad", Range.class));
		list = new List<Range>("ranges", ranges, 5);
		innerList = new ComponentFinder(list).find("ranges", JList.class);
	}

	@Test
	public void shouldNotAllowMultipleRowsSelection() {
		assertEquals(ListSelectionModel.SINGLE_SELECTION, innerList.getSelectionModel().getSelectionMode());
	}

	@Test
	public void shouldNotHaveSelectionByDefault() {
		assertFalse(list.hasSelection());
	}

	@Test
	public void checkSelectedObject() {
		innerList.getSelectionModel().setSelectionInterval(0, 0);
		assertTrue(list.hasSelection());
		assertSame(ranges.first(), list.getSelected());
	}

	@Test
	public void testEnabled() {
		assertTrue(innerList.isEnabled());
		list.setEnabled(false);
		assertFalse(list.isEnabled());
		assertFalse(innerList.isEnabled());
	}

	@Test
	public void shouldAddListListener() throws Exception {
		ListListener<Range> listener = PowerMockito.mock(ListListener.class);
		ListComponentAdapter<Range> adapter = PowerMockito.mock(ListComponentAdapter.class);
		PowerMockito.whenNew(ListComponentAdapter.class).withArguments(listener, list).thenReturn(adapter);

		list.addListListener(listener);
		assertTrue(asList(innerList.getMouseListeners()).contains(adapter));
		DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) innerList.getSelectionModel();
		assertTrue(asList(selectionModel.getListSelectionListeners()).contains(adapter));
	}
}