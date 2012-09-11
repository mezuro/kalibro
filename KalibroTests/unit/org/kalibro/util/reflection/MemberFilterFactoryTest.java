package org.kalibro.util.reflection;

import static java.lang.reflect.Modifier.*;
import static org.junit.Assert.*;
import static org.kalibro.util.reflection.MemberFilterFactory.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kalibro.UtilityClassTest;

public class MemberFilterFactoryTest extends UtilityClassTest {

	private Member setTestEnvironment, setUp;

	private MemberFilter filter;

	@Before
	public void setUp() throws Exception {
		setUp = getClass().getMethod("setUp");
		setTestEnvironment = getClass().getMethod("setTestEnvironment");
	}

	@Override
	protected Class<?> utilityClass() {
		return MemberFilterFactory.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateModifierMemberFilter() {
		filter = is(PUBLIC);
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));

		filter = is(STATIC);
		assertFalse(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));

		filter = is(SYNCHRONIZED);
		assertFalse(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAnnotationMemberFilter() {
		filter = hasAnnotation(Before.class);
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));

		filter = hasAnnotation(BeforeClass.class);
		assertFalse(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateNameMemberFilter() {
		filter = nameMatches("set.*");
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));

		filter = named("setUp");
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateNotMemberFilter() {
		filter = not(is(STATIC));
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));

		filter = not(is(PRIVATE));
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));

		filter = not(is(PUBLIC));
		assertFalse(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateAndMemberFilter() {
		filter = and(is(STATIC), hasAnnotation(BeforeClass.class));
		assertFalse(filter.accept(setUp));
		assertTrue(filter.accept(setTestEnvironment));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateOrMemberFilter() {
		filter = or(hasAnnotation(Before.class), not(is(STATIC)));
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(setTestEnvironment));
	}
}