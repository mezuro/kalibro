package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.powermock.reflect.Whitebox;

public abstract class KalibroTestCase implements Timeouts {

	@BeforeClass
	public static void setTestEnvironment() {
		Whitebox.setInternalState(Environment.class, "current", Environment.TEST);
	}

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

	protected File samplesDirectory() {
		return new File(Environment.dotKalibro(), "samples");
	}

	protected File projectsDirectory() {
		return new File(Environment.dotKalibro(), "projects");
	}

	protected File helloWorldDirectory() {
		return new File(projectsDirectory(), "HelloWorld-1.0");
	}

	protected void assertDifferent(Object... objects) {
		for (int i = 0; i < objects.length - 1; i++)
			for (int j = i + 1; j < objects.length; j++)
				assertFalse("Not all different: " + Arrays.asList(objects), objects[i].equals(objects[j]));
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

	protected void assertMatches(String regularExpression, String string) {
		assertMatches(Pattern.compile(regularExpression), string);
	}

	protected void assertMatches(Pattern pattern, String string) {
		String message = "Expected to match expression:\n" + pattern.pattern() + "\nbut was:\n" + string;
		assertTrue(message, pattern.matcher(string).matches());
	}

	protected void checkException(Task task, Class<? extends Throwable> exceptionClass) {
		checkKalibroException(task, "Error while running task: null", exceptionClass);
	}

	protected void checkKalibroError(Task task, String message) {
		checkKalibroError(task, message, null);
	}

	protected void checkKalibroError(Task task, String message, Class<? extends Throwable> causeClass) {
		checkKalibroException(task, KalibroError.MESSAGE_PREFIX + message, causeClass);
	}

	protected void checkKalibroException(Task task, String message) {
		checkKalibroException(task, message, null);
	}

	protected void checkKalibroException(Task task, String message, Class<? extends Throwable> causeClass) {
		try {
			task.executeAndWait();
			fail("Should throw KalibroException on executing task");
		} catch (KalibroException exception) {
			assertEquals(message, exception.getMessage());
			Throwable cause = exception.getCause();
			if (causeClass == null)
				assertNull("Cause not expected: " + cause, cause);
			else
				assertClassEquals(causeClass, cause);
		}
	}

	protected void assertClassEquals(Class<?> expectedClass, Object object) {
		assertEquals(expectedClass, object.getClass());
	}

	protected void assertDoubleEquals(Double expected, Double actual) {
		assertEquals(expected, actual, 1E-10);
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