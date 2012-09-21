package org.kalibro.core.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class OrMemberFilterTest extends TestCase {

	private Member member;
	private MemberFilter trueFilter, falseFilter, errorFilter;

	@Before
	public void setUp() {
		member = mock(Member.class);
		trueFilter = mock(MemberFilter.class);
		falseFilter = mock(MemberFilter.class);
		errorFilter = mock(MemberFilter.class);
		when(trueFilter.accept(member)).thenReturn(true);
		when(falseFilter.accept(member)).thenReturn(false);
		doThrow(new NullPointerException()).when(errorFilter).accept(member);
	}

	@Test
	public void checkTrueTableForTwo() {
		assertFalse(new OrMemberFilter(falseFilter, falseFilter).accept(member));
		assertTrue(new OrMemberFilter(falseFilter, trueFilter).accept(member));
		assertTrue(new OrMemberFilter(trueFilter, falseFilter).accept(member));
		assertTrue(new OrMemberFilter(trueFilter, trueFilter).accept(member));
	}

	@Test
	public void shouldReturnTrueOnFirstAccept() {
		assertTrue(new OrMemberFilter(falseFilter, trueFilter, errorFilter).accept(member));
		verifyZeroInteractions(errorFilter);
	}
}