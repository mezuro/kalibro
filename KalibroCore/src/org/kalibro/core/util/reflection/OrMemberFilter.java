package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

public class OrMemberFilter implements MemberFilter {

	private MemberFilter[] filters;

	public OrMemberFilter(MemberFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(Member member) {
		boolean accept = false;
		for (MemberFilter filter : filters)
			accept = accept || filter.accept(member);
		return accept;
	}
}