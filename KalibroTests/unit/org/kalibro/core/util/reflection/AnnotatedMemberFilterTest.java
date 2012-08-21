package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.AccessibleObject;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AnnotatedMemberFilterTest extends KalibroTestCase {

	private AccessibleObject member;
	private AnnotatedMemberFilter filter;

	@Before
	public void setUp() {
		member = mock(AccessibleObject.class);
		filter = new AnnotatedMemberFilter(Before.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptAnnotatedMember() {
		when(member.isAnnotationPresent(Before.class)).thenReturn(true);
		assertTrue(filter.accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptMemberWithoutAnnotation() {
		when(member.isAnnotationPresent(Before.class)).thenReturn(false);
		assertFalse(filter.accept(member));
	}
}