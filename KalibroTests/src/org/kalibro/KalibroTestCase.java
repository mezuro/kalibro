package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.util.Directories;

public abstract class KalibroTestCase {

	public static final File TESTS_DIRECTORY = new File(Directories.kalibro(), "tests");
	public static final File SAMPLES_DIRECTORY = new File(TESTS_DIRECTORY, "samples");
	public static final File PROJECTS_DIRECTORY = new File(TESTS_DIRECTORY, "projects");
	public static final File HELLO_WORLD_DIRECTORY = new File(PROJECTS_DIRECTORY, "HelloWorld-1.0");

	protected static final int UNIT_TIMEOUT = 750;
	protected static final int INTEGRATION_TIMEOUT = 2500;
	protected static final int ACCEPTANCE_TIMEOUT = 10000;

	private boolean waiting;

	protected void waitNotification() throws InterruptedException {
		waiting = true;
		while (waiting)
			wait();
	}

	protected void notifyTest() {
		waiting = false;
		notify();
	}

	protected void assertDoubleEquals(Double expected, Double actual) {
		assertEquals(expected, actual, 1E-10);
	}

	protected <E extends Comparable<? super E>> void assertSorted(E... sortedElements) {
		assertSorted(Arrays.asList(sortedElements));
	}

	protected <E extends Comparable<? super E>> void assertSorted(List<E> sortedList) {
		List<E> expected = new ArrayList<E>(sortedList);
		List<E> actual = new ArrayList<E>(sortedList);
		Collections.shuffle(actual);
		Collections.sort(actual);
		assertEquals(expected, actual);
	}

	protected <T> void assertDeepEquals(Collection<T> actual, T... expected) {
		assertDeepEquals(Arrays.asList(expected), actual);
	}

	protected <T> void assertDeepEquals(Collection<T> expected, Collection<T> actual) {
		assertDeepEquals(new CollectionWrapper<T>(expected), new CollectionWrapper<T>(actual));
	}

	protected <K, V> void assertDeepEquals(Map<K, V> expected, Map<K, V> actual) {
		assertDeepEquals(new MapWrapper<K, V>(expected), new MapWrapper<K, V>(actual));
	}

	protected void assertDeepEquals(AbstractEntity<?> expected, AbstractEntity<?> actual) {
		if (!expected.deepEquals(actual)) {
			String actualText = (actual == null) ? "null" : actual.deepPrint();
			String expectedText = expected.deepPrint();
			assertEquals(expectedText, actualText);
			fail("EXPECTED:\n" + expected.deepPrint() + "\nBUT WAS:\n" + actualText);
		}
	}

	protected void checkException(Task errorTask, Class<? extends Exception> exceptionClass) {
		checkException(errorTask, exceptionClass, null);
	}

	protected void checkException(Task errorTask, Class<? extends Exception> exceptionClass, String message) {
		checkException(errorTask, exceptionClass, message, null);
	}

	protected void checkException(Task errorTask, Class<? extends Exception> exceptionClass, String message,
		Class<? extends Exception> causeClass) {
		try {
			errorTask.perform();
			fail("Preceding line should throw Exception");
		} catch (Exception exception) {
			assertEquals(exceptionClass, exception.getClass());
			if (message != null)
				assertEquals(message, exception.getMessage());
			if (causeClass != null)
				assertClassEquals(causeClass, exception.getCause());
		}
	}

	protected void assertClassEquals(Class<?> expectedClass, Object object) {
		assertEquals(expectedClass, object.getClass());
	}

	private class CollectionWrapper<T> extends AbstractEntity<CollectionWrapper<T>> {

		@IdentityField
		@SuppressWarnings("unused" /* read by EntityReflector */)
		private Collection<T> collection;

		public CollectionWrapper(Collection<T> collection) {
			this.collection = collection;
		}
	}

	private class MapWrapper<K, V> extends AbstractEntity<MapWrapper<K, V>> {

		@IdentityField
		@SuppressWarnings("unused" /* read by EntityReflector */)
		private Map<K, V> map;

		public MapWrapper(Map<K, V> map) {
			this.map = map;
		}
	}
}