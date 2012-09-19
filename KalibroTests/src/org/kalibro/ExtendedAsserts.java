package org.kalibro;

import static org.junit.Assert.*;

import java.util.*;

import org.kalibro.core.abstractentity.Equality;
import org.kalibro.core.abstractentity.Printer;
import org.kalibro.core.concurrent.Task;
import org.powermock.api.mockito.PowerMockito;

abstract class ExtendedAsserts extends PowerMockito {

	protected static void assertClassEquals(Class<?> expectedClass, Object object) {
		assertEquals(expectedClass, object.getClass());
	}

	protected static void assertDifferent(Object... elements) {
		for (int i = 0; i < elements.length - 1; i++)
			for (int j = i + 1; j < elements.length; j++)
				assertFalse("Elements at " + i + " and at " + j + " are equal.", elements[i].equals(elements[j]));
	}

	protected static void assertDoubleEquals(Double expected, Double actual) {
		assertEquals(expected, actual, 1E-10);
	}

	protected static <E extends Comparable<? super E>> void assertSorted(E... elements) {
		int i = 0;
		while (i < elements.length - 1)
			assertTrue("Element " + i + " is greater than its successor.", elements[i].compareTo(elements[++i]) <= 0);
	}

	protected static <T> void assertDeepCollection(Collection<T> actual, T... expected) {
		List<T> expectedList = Arrays.asList(expected);
		if (actual instanceof List)
			assertDeepEquals(expectedList, actual);
		else
			assertDeepEquals(new HashSet<T>(expectedList), new HashSet<T>(actual));
	}

	protected static <T> void assertDeepList(List<T> actual, T... expected) {
		assertDeepEquals(Arrays.asList(expected), actual);
	}

	protected static <T> void assertDeepSet(Set<T> actual, T... expected) {
		assertDeepEquals(new HashSet<T>(Arrays.asList(expected)), actual);
	}

	protected static <T> void assertDeepEquals(Object expected, Object actual) {
		if (!Equality.areDeepEqual(expected, actual)) {
			assertEquals(Printer.print(expected), Printer.print(actual));
			fail("Print is the same but they are not deep equal");
		}
	}

	protected static TaskMatcher assertThat(Task<?> task) {
		return new TaskMatcher(task);
	}
}