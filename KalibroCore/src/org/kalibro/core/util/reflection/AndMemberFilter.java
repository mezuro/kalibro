package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

public class AndMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	public AndMemberFilter(MemberFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(Member member) {
		boolean accept = true;
		for (MemberFilter filter : filters)
			accept = accept && filter.accept(member);
		return accept;
	}
}