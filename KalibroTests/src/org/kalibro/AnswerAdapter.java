package org.kalibro;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class AnswerAdapter implements Answer<Object> {

	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		answer();
		return null;
	}

	protected abstract void answer() throws Throwable;
}