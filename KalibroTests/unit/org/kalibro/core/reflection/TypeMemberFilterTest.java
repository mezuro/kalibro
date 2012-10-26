package org.kalibro.core.reflection;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.kalibro.tests.UnitTest;

public class TypeMemberFilterTest extends UnitTest {

	private TypeMemberFilter filter;

	@Before
	public void setUp() {
		filter = new TypeMemberFilter(MethodRule.class);
	}

	@Test
	public void shouldAcceptTypedMember() {
		assertTrue(filter.accept(field(getClass(), "testTimeout")));
		assertTrue(filter.accept(method(getClass(), "testTimeout")));
	}

	@Test
	public void shouldNotAcceptMemberWithOtherType() {
		assertFalse(filter.accept(field(getClass(), "filter")));
		assertFalse(filter.accept(method(getClass(), "setUp")));
	}
}