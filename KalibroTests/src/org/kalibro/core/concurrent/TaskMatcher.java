package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.kalibro.ExtendedAsserts;
import org.kalibro.KalibroError;
import org.kalibro.KalibroException;
import org.kalibro.ThrowableMatcher;

public class TaskMatcher {

	private Task<?> task;

	public TaskMatcher(Task<?> task) {
		this.task = task;
	}

	public ThrowableMatcher throwsError() {
		return doThrow(KalibroError.class);
	}

	public ThrowableMatcher throwsException() {
		return doThrow(KalibroException.class);
	}

	public ThrowableMatcher doThrow(Class<? extends Throwable> throwableClass) {
		Throwable throwed = doCatch(throwableClass);
		ExtendedAsserts.assertClassEquals(throwableClass, throwed);
		return new ThrowableMatcher(throwed);
	}

	public void doThrow(Throwable throwable) {
		assertSame(throwable, doCatch(throwable));
	}

	private Throwable doCatch(Object expected) {
		try {
			task.compute();
			fail("Expected but not throwed:\n" + expected);
			return null;
		} catch (Throwable throwed) {
			return throwed;
		}
	}
}