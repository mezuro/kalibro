package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AnnotatedMemberFilterTest extends KalibroTestCase {

	private Member member;
	private AnnotatedMemberFilter filter;

	@Before
	public void setUp() {
		filter = new AnnotatedMemberFilter(Before.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptAnnotatedMember() throws Exception {
		member = getClass().getMethod("setUp");
		assertTrue(filter.accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptMemberWithOtherAnnotation() throws Exception {
		member = getClass().getMethod("shouldAcceptAnnotatedMember");
		assertFalse(filter.accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNotAnnotatedMember() throws Exception {
		member = getClass().getDeclaredField("filter");
		assertFalse(filter.accept(member));
	}
}