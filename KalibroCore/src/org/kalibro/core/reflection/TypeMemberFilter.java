package org.kalibro.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * This filter accepts members with the specified type.
 * 
 * @author Carlos Morais
 */
final class TypeMemberFilter implements MemberFilter {

	private Class<?> type;

	TypeMemberFilter(Class<?> type) {
		this.type = type;
	}

	@Override
	public boolean accept(Member member) {
		return type.isAssignableFrom(getType(member));
	}

	private Class<?> getType(Member member) {
		if (member instanceof Field)
			return ((Field) member).getType();
		return ((Method) member).getReturnType();
	}
}