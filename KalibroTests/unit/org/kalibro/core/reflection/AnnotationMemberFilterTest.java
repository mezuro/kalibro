package org.kalibro.core.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.AccessibleObject;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AnnotationMemberFilterTest extends UnitTest {

	private AccessibleObject member;
	private AnnotationMemberFilter filter;

	@Before
	public void setUp() {
		member = mock(AccessibleObject.class);
		filter = new AnnotationMemberFilter(Before.class);
	}

	@Test
	public void shouldAcceptAnnotatedMember() {
		when(member.isAnnotationPresent(Before.class)).thenReturn(true);
		assertTrue(filter.accept(member));
	}

	@Test
	public void shouldNotAcceptMemberWithoutAnnotation() {
		when(member.isAnnotationPresent(Before.class)).thenReturn(false);
		assertFalse(filter.accept(member));
	}
}