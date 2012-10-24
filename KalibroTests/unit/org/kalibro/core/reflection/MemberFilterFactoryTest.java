package org.kalibro.core.reflection;

import static java.lang.reflect.Modifier.*;
import static org.junit.Assert.*;
import static org.kalibro.core.reflection.MemberFilterFactory.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UtilityClassTest;

public class MemberFilterFactoryTest extends UtilityClassTest {

	private Member never, setUp;

	private MemberFilter filter;

	@Before
	public void setUp() throws Exception {
		setUp = getClass().getMethod("setUp");
		never = getClass().getMethod("never");
	}

	@Override
	protected Class<?> utilityClass() {
		return MemberFilterFactory.class;
	}

	@Test
	public void shouldCreateModifierMemberFilter() {
		filter = is(PUBLIC);
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(never));

		filter = is(STATIC);
		assertFalse(filter.accept(setUp));
		assertTrue(filter.accept(never));

		filter = is(SYNCHRONIZED);
		assertFalse(filter.accept(setUp));
		assertFalse(filter.accept(never));
	}

	@Test
	public void shouldCreateAnnotationMemberFilter() {
		filter = hasAnnotation(Before.class);
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(never));

		filter = hasAnnotation(Test.class);
		assertFalse(filter.accept(setUp));
		assertFalse(filter.accept(never));
	}

	@Test
	public void shouldCreateNameMemberFilter() {
		filter = nameMatches(".*e.*");
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(never));

		filter = named("setUp");
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(never));
	}

	@Test
	public void shouldCreateNotMemberFilter() {
		filter = not(is(STATIC));
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(never));

		filter = not(is(PRIVATE));
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(never));

		filter = not(is(PUBLIC));
		assertFalse(filter.accept(setUp));
		assertFalse(filter.accept(never));
	}

	@Test
	public void shouldCreateAndMemberFilter() {
		filter = and(not(is(STATIC)), hasAnnotation(Before.class));
		assertTrue(filter.accept(setUp));
		assertFalse(filter.accept(never));
	}

	@Test
	public void shouldCreateOrMemberFilter() {
		filter = or(hasAnnotation(Before.class), is(STATIC));
		assertTrue(filter.accept(setUp));
		assertTrue(filter.accept(never));
	}
}