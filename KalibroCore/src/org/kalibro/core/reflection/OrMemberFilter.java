package org.kalibro.core.reflection;

import java.lang.reflect.Member;

/**
 * This filter produces a logical OR of the specified filters.
 * 
 * @author Carlos Morais
 */
final class OrMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	OrMemberFilter(MemberFilter... filters) {
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