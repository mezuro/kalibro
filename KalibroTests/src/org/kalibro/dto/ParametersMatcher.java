package org.kalibro.dto;

import org.kalibro.core.abstractentity.Equality;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;

final class ParametersMatcher extends ArgumentMatcher<Object[]> implements VarargMatcher {

	private LazyLoadExpectation expectation;

	ParametersMatcher(LazyLoadExpectation expectation) {
		this.expectation = expectation;
	}

	@Override
	public boolean matches(Object parameters) {
		return Equality.areDeepEqual(expectation.parameters, parameters);
	}
}