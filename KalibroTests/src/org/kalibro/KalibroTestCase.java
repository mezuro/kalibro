package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.kalibro.core.Environment;
import org.kalibro.core.abstractentity.Equality;
import org.kalibro.core.concurrent.Task;
import org.powermock.reflect.Whitebox;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

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

	protected String loadResource(String name) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(name));
	}

	protected <T> T loadFixture(String name, Class<T> type) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.loadAs(getClass().getResourceAsStream(name + ".yml"), type);
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
		try {
			task.executeAndWait();
			fail("Did not throw expected error");
		} catch (KalibroException exception) {
			assertClassEquals(KalibroError.class, exception.getCause());
			KalibroError error = (KalibroError) exception.getCause();
			assertEquals(message, error.getMessage());
			Throwable cause = error.getCause();
			if (causeClass == null)
				assertNull("Unexpected cause: " + cause, cause);
			else
				assertClassEquals(causeClass, cause);
		}
	}

	protected void checkKalibroException(Task task, String message) {
		checkKalibroException(task, message, null);
	}

	protected void checkKalibroException(Task task, String message, Class<? extends Throwable> causeClass) {
		try {
			task.executeAndWait();
			fail("Did not throw expected exception");
		} catch (KalibroException exception) {
			assertEquals(message, exception.getMessage());
			Throwable cause = exception.getCause();
			if (causeClass == null)
				assertNull("Unexpected cause: " + cause, cause);
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

	protected <T> void assertDeepCollection(Collection<T> actual, T... expected) {
		List<T> expectedList = Arrays.asList(expected);
		if (actual instanceof List)
			assertDeepEquals(expectedList, actual);
		else
			assertDeepEquals(new HashSet<T>(expectedList), new HashSet<T>(actual));
	}

	protected <T> void assertDeepSet(Set<T> actual, T... expected) {
		assertDeepEquals(new HashSet<T>(Arrays.asList(expected)), actual);
	}

	protected <T> void assertDeepList(List<T> actual, T... expected) {
		assertDeepEquals(Arrays.asList(expected), actual);
	}

	protected <T> void assertDeepEquals(Object expected, Object actual) {
		if (!Equality.areDeepEqual(expected, actual)) {
			assertEquals(expected.toString(), actual.toString());
			fail("EXPECTED:\n" + expected + "\nBUT WAS:\n" + actual);
		}
	}
}