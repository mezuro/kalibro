package org.kalibro;

import static org.junit.Assert.*;

import org.kalibro.core.concurrent.Task;

public class TaskMatcher {

	private Task task;

	protected TaskMatcher(Task task) {
		this.task = task;
	}

	public ThrowableMatcher throwsError() {
		return doThrow(KalibroError.class);
	}

	public ThrowableMatcher throwsException() {
		return doThrow(KalibroException.class);
	}

	public ThrowableMatcher doThrow(Class<? extends Throwable> throwableClass) {
		Throwable throwed = invokeAndCatch(throwableClass);
		ExtendedAsserts.assertClassEquals(throwableClass, throwed);
		return new ThrowableMatcher(throwed);
	}

	public void doThrow(Throwable throwable) {
		assertSame(throwable, invokeAndCatch(throwable));
	}

	private Throwable invokeAndCatch(Object expected) {
		try {
			task.perform();
			fail("Expected but not throwed:\n" + expected);
			return null;
		} catch (Throwable throwed) {
			return throwed;
		}
	}
}