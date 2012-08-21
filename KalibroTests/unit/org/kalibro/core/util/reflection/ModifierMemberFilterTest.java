package org.kalibro.core.util.reflection;

import static java.lang.reflect.Modifier.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.lang.reflect.Member;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class ModifierMemberFilterTest extends KalibroTestCase {

	private Member member;

	@Before
	public void setUp() {
		member = mock(Member.class);
		when(member.getModifiers()).thenReturn(PROTECTED | STATIC | SYNCHRONIZED);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAcceptMemberWithModifier() {
		assertTrue(new ModifierMemberFilter(PROTECTED).accept(member));
		assertTrue(new ModifierMemberFilter(STATIC).accept(member));
		assertTrue(new ModifierMemberFilter(SYNCHRONIZED).accept(member));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotAcceptMemberWithoutModifier() {
		assertFalse(new ModifierMemberFilter(PRIVATE).accept(member));
		assertFalse(new ModifierMemberFilter(FINAL).accept(member));
		assertFalse(new ModifierMemberFilter(STRICT).accept(member));
	}
}