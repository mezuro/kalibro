package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;

import org.junit.Test;
import org.kalibro.TestCase;

public class NotMemberFilterTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
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