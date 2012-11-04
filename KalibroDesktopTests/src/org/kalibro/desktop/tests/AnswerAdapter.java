package org.kalibro.desktop.tests;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class AnswerAdapter implements Answer<Void> {

	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		answer();
		return null;
	}

	protected abstract void answer() throws Throwable;
}