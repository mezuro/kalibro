package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class OrMemberFilterTest extends KalibroTestCase {

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

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTrueTableForTwo() {
		assertFalse(new OrMemberFilter(falseFilter, falseFilter).accept(member));
		assertTrue(new OrMemberFilter(falseFilter, trueFilter).accept(member));
		assertTrue(new OrMemberFilter(trueFilter, falseFilter).accept(member));
		assertTrue(new OrMemberFilter(trueFilter, trueFilter).accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnTrueOnFirstAccept() {
		assertTrue(new OrMemberFilter(falseFilter, trueFilter, errorFilter).accept(member));
	}
}