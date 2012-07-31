package org.kalibro.core.util.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

public abstract class MemberFilterAdapter implements MemberFilter {

	@Override
	public boolean accept(Member member) {
		return accept((AccessibleObject) member);
	}

	protected abstract boolean accept(AccessibleObject member);
}