package org.kalibro.desktop.swingextension.list;

import static org.junit.Assert.*;

import java.util.SortedSet;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Reading;
import org.kalibro.ReadingGroup;
import org.kalibro.desktop.tests.ComponentFinder;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest(List.class)
public class ListTest extends UnitTest {

	private SortedSet<Reading> readings;
	private JList<Reading> innerList;

	private List<Reading> list;

	@Before
	public void setUp() {
		readings = loadFixture("scholar", ReadingGroup.class).getReadings();
		list = new List<Reading>("readings", readings, 5);
		innerList = new ComponentFinder(list).find("readings", JList.class);
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
		assertSame(readings.first(), list.getSelected());
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
		ListListener<Reading> listener = mock(ListListener.class);
		ListComponentAdapter<Reading> adapter = mock(ListComponentAdapter.class);
		whenNew(ListComponentAdapter.class).withArguments(listener, list).thenReturn(adapter);

		list.addListListener(listener);
		assertTrue(list(innerList.getMouseListeners()).contains(adapter));
		DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) innerList.getSelectionModel();
		assertTrue(list(selectionModel.getListSelectionListeners()).contains(adapter));
	}
}