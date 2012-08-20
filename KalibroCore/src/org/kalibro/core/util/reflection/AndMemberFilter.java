package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

/**
 * This filter produces a logical AND of the filters specified.
 * 
 * @author Carlos Morais
 */
final class AndMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	public AndMemberFilter(MemberFilter... filters) {
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