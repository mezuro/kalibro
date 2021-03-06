package org.kalibro.core.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AndMemberFilterTest extends UnitTest {

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
		assertFalse(new AndMemberFilter(falseFilter, falseFilter).accept(member));
		assertFalse(new AndMemberFilter(falseFilter, trueFilter).accept(member));
		assertFalse(new AndMemberFilter(trueFilter, falseFilter).accept(member));
		assertTrue(new AndMemberFilter(trueFilter, trueFilter).accept(member));
	}

	@Test
	public void shouldReturnTrueOnFirstReject() {
		assertTrue(new OrMemberFilter(trueFilter, falseFilter, errorFilter).accept(member));
		verifyZeroInteractions(errorFilter);
	}
}