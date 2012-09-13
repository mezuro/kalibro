package org.kalibro;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.Pattern;

import org.kalibro.core.abstractentity.Equality;
import org.kalibro.core.abstractentity.Printer;
import org.kalibro.core.concurrent.Task;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;

abstract class TestShortcuts extends PowerMockito {

	protected void assertClassEquals(Class<?> expectedClass, Object object) {
		assertEquals(expectedClass, object.getClass());
	}

	protected void assertDifferent(Object... elements) {
		for (int i = 0; i < elements.length - 1; i++)
			for (int j = i + 1; j < elements.length; j++)
				assertFalse("Elements at " + i + " and at " + j + " are equal.", elements[i].equals(elements[j]));
	}

	protected void assertDoubleEquals(Double expected, Double actual) {
		assertEquals(expected, actual, 1E-10);
	}

	protected void assertMatches(String regularExpression, String string) {
		assertMatches(Pattern.compile(regularExpression), string);
	}

	protected void assertMatches(Pattern pattern, String string) {
		String failMessage = "String:\n" + string + "\ndoes not match pattern:\n" + pattern.pattern();
		if (string != null)
			assertTrue(failMessage, pattern.matcher(string).matches());
		else if (!pattern.toString().equals(".*"))
			fail(failMessage);
	}

	protected <E extends Comparable<? super E>> void assertSorted(E... elements) {
		int i = 0;
		while (i < elements.length - 1)
			assertTrue("Element " + i + " is greater than its successor.", elements[i].compareTo(elements[++i]) <= 0);
	}

	protected <T> void assertDeepCollection(Collection<T> actual, T... expected) {
		List<T> expectedList = Arrays.asList(expected);
		if (actual instanceof List)
			assertDeepEquals(expectedList, actual);
		else
			assertDeepEquals(new HashSet<T>(expectedList), new HashSet<T>(actual));
	}

	protected <T> void assertDeepList(List<T> actual, T... expected) {
		assertDeepEquals(Arrays.asList(expected), actual);
	}

	protected <T> void assertDeepSet(Set<T> actual, T... expected) {
		assertDeepEquals(new HashSet<T>(Arrays.asList(expected)), actual);
	}

	protected <T> void assertDeepEquals(Object expected, Object actual) {
		if (!Equality.areDeepEqual(expected, actual)) {
			assertEquals(Printer.print(expected), Printer.print(actual));
			fail("Print is the same but they are not deep equal");
		}
	}

	protected void assertThrowsError(Task task, String message) {
		assertThrows(task, KalibroError.class, message);
	}

	protected void assertThrowsError(Task task, String message, Class<? extends Throwable> causeClass) {
		assertThrows(task, KalibroError.class, message, causeClass);
	}

	protected void assertThrowsException(Task task, String message) {
		assertThrows(task, KalibroException.class, message);
	}

	protected void assertThrowsException(Task task, String message, Class<? extends Throwable> causeClass) {
		assertThrows(task, KalibroException.class, message, causeClass);
	}

	protected void assertThrows(Task task, Class<? extends Throwable> exceptionClass) {
		Pattern pattern = Pattern.compile(".*", Pattern.MULTILINE);
		assertThrows(task, exceptionClass, pattern, null);
	}

	protected void assertThrows(Task task, Class<? extends Throwable> exceptionClass, String message) {
		assertThrows(task, exceptionClass, message, null);
	}

	protected void assertThrows(Task task, Class<? extends Throwable> exceptionClass, String message,
		Class<? extends Throwable> causeClass) {
		assertThrows(task, exceptionClass, Pattern.compile(Pattern.quote(message)), causeClass);
	}

	private void assertThrows(Task task, Class<? extends Throwable> exceptionClass, Pattern messagePattern,
		Class<? extends Throwable> causeClass) {
		try {
			task.perform();
			fail("Expected but not throwed:\n" + exceptionClass.getName() + ": " + messagePattern);
		} catch (Throwable throwed) {
			assertClassEquals(exceptionClass, throwed);
			assertMatches(messagePattern, throwed.getMessage());
			Throwable cause = throwed.getCause();
			if (causeClass == null)
				assertNull("Unexpected cause: " + cause, cause);
			else
				assertClassEquals(causeClass, cause);
		}
	}

	protected <T> T verify(T mock) {
		return Mockito.verify(mock);
	}

	protected <T> T verify(T mock, VerificationMode mode) {
		return Mockito.verify(mock, mode);
	}

	protected VerificationMode never() {
		return Mockito.never();
	}

	protected VerificationMode once() {
		return Mockito.times(1);
	}

	protected VerificationMode times(int times) {
		return Mockito.times(times);
	}
}