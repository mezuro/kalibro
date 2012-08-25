package org.kalibro.core.abstractentity;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class EqualArgumentsAnswer implements Answer<Boolean> {

	@Override
	public Boolean answer(InvocationOnMock invocation) throws Throwable {
		Object[] arguments = invocation.getArguments();
		return arguments[0].equals(arguments[1]);
	}
}