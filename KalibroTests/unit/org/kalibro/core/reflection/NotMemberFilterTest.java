package org.kalibro.core.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NotMemberFilterTest extends UnitTest {

	@Test
	public void shouldNegateFilter() {
		Member member = mock(Member.class);
		MemberFilter filter = mock(MemberFilter.class);
		MemberFilter notFilter = new NotMemberFilter(filter);

		when(filter.accept(member)).thenReturn(false);
		assertTrue(notFilter.accept(member));

		when(filter.accept(member)).thenReturn(true);
		assertFalse(notFilter.accept(member));
	}
}