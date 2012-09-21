package org.kalibro.core.reflection;

import java.lang.reflect.Member;

/**
 * This filter produces a logical AND of the specified filters.
 * 
 * @author Carlos Morais
 */
final class AndMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	protected AndMemberFilter(MemberFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(Member member) {
		for (MemberFilter filter : filters)
			if (!filter.accept(member))
				return false;
		return true;
	}
}