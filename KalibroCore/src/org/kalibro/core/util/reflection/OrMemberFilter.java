package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

/**
 * This filter produces a logical OR of the filters specified.
 * 
 * @author Carlos Morais
 */
final class OrMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	protected OrMemberFilter(MemberFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(Member member) {
		for (MemberFilter filter : filters)
			if (filter.accept(member))
				return true;
		return false;
	}
}