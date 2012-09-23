package org.kalibro.dto;

import org.junit.Assert;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;

final class ParametersMatcher extends ArgumentMatcher<Object[]> implements VarargMatcher {

	private LazyLoadExpectation expectation;

	ParametersMatcher(LazyLoadExpectation expectation) {
		this.expectation = expectation;
	}

	@Override
	public boolean matches(Object parameters) {
		try {
			Assert.assertArrayEquals(expectation.parameters, (Object[]) parameters);
			return true;
		} catch (AssertionError error) {
			return false;
		}
	}
}