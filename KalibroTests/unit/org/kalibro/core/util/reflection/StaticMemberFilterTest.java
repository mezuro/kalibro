package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StaticMemberFilterTest extends KalibroTestCase {

	private Member member;
	private StaticMemberFilter filter;

	@Before
	public void setUp() {
		member = mock(Member.class);
		filter = new StaticMemberFilter();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptStaticMember() {
		when(member.getModifiers()).thenReturn(Modifier.STATIC);
		assertTrue(filter.accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptNonStaticMember() {
		when(member.getModifiers()).thenReturn(0);
		assertFalse(filter.accept(member));
	}
}