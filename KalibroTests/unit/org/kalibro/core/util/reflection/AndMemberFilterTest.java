package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class AndMemberFilterTest extends KalibroTestCase {

	private Member member;
	private MemberFilter trueFilter, falseFilter;

	@Before
	public void setUp() {
		member = mock(Member.class);
		trueFilter = mock(MemberFilter.class);
		falseFilter = mock(MemberFilter.class);
		when(trueFilter.accept(member)).thenReturn(true);
		when(falseFilter.accept(member)).thenReturn(false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTrueTableForTwo() {
		assertFalse(new AndMemberFilter(falseFilter, falseFilter).accept(member));
		assertFalse(new AndMemberFilter(falseFilter, trueFilter).accept(member));
		assertFalse(new AndMemberFilter(trueFilter, falseFilter).accept(member));
		assertTrue(new AndMemberFilter(trueFilter, trueFilter).accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNeedJustOneFalseToBeFalse() {
		assertTrue(new AndMemberFilter(trueFilter, trueFilter, trueFilter, trueFilter, trueFilter).accept(member));
		assertFalse(new AndMemberFilter(trueFilter, trueFilter, trueFilter, trueFilter, falseFilter).accept(member));
	}
}