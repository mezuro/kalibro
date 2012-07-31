package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

final class StaticMemberFilter implements MemberFilter {

	@Override
	public boolean accept(Member member) {
		return Modifier.isStatic(member.getModifiers());
	}
}