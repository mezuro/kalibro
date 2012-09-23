package org.kalibro.core.reflection;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * This filter accepts members with the specified {@link Modifier}.
 * 
 * @author Carlos Morais
 */
final class ModifierMemberFilter implements MemberFilter {

	private int modifier;

	protected ModifierMemberFilter(int modifier) {
		this.modifier = modifier;
	}

	@Override
	public boolean accept(Member member) {
		return (member.getModifiers() & modifier) != 0;
	}
}