package org.kalibro.core.util.reflection;

import static org.junit.Assert.*;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class MemberFilterFactoryTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		Constructor<MemberFilterFactory> constructor = MemberFilterFactory.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	private Member emmaCoverage, setUp;

	@Before
	public void setUp() throws Exception {
		setUp = getClass().getMethod("setUp");
		emmaCoverage = getClass().getMethod("emmaCoverage");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateStaticMemberFilter() {
		assertClassEquals(StaticMemberFilter.class, isStatic());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAnnotatedMemberFilter() {
		MemberFilter filter = isAnnotatedWith(Before.class);
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(emmaCoverage));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateNotMemberFilter() {
		MemberFilter filter = not(isStatic());
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(emmaCoverage));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAndMemberFilter() {
		MemberFilter filter = and(isStatic(), isAnnotatedWith(BeforeClass.class));
		assertFalse(filter.accept(setUp));
		assertTrue(filter.accept(emmaCoverage));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateOrMemberFilter() {
		MemberFilter filter = or(isAnnotatedWith(Before.class), isAnnotatedWith(BeforeClass.class));
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(emmaCoverage));
	}
}