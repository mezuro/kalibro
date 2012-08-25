package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class NameMemberFilterTest extends TestCase {

	private Member member;
	private NameMemberFilter filter;

	@Before
	public void setUp() {
		member = mock(Member.class);
		filter = new NameMemberFilter(".*Member.*");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptMemberIfNameMatches() {
		when(member.getName()).thenReturn("Member");
		assertTrue(filter.accept(member));

		when(member.getName()).thenReturn("MemberFilter");
		assertTrue(filter.accept(member));

		when(member.getName()).thenReturn("MyMember");
		assertTrue(filter.accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptMemberIfNameDoesNotMatch() {
		when(member.getName()).thenReturn("ember");
		assertFalse(filter.accept(member));

		when(member.getName()).thenReturn("Memb");
		assertFalse(filter.accept(member));

		when(member.getName()).thenReturn("Something");
		assertFalse(filter.accept(member));
	}
}