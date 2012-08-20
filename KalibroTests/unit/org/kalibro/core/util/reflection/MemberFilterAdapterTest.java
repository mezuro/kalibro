package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class MemberFilterAdapterTest extends KalibroTestCase {

	private MemberFilter filter;

	@Before
	public void setUp() {
		filter = new MemberFilterAdapter() {

			@Override
			protected boolean accept(AccessibleObject member) {
				return member.isAccessible();
			}
		};
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFilter() throws Exception {
		Field field = getClass().getDeclaredField("filter");
		assertFalse(filter.accept(field));

		field.setAccessible(true);
		assertTrue(filter.accept(field));
	}
}