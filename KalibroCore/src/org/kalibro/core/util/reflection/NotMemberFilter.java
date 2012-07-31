package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

public class NotMemberFilter implements MemberFilter {

	private MemberFilter filter;

	public NotMemberFilter(MemberFilter filter) {
		this.filter = filter;
	}

	@Override
	public boolean accept(Member member) {
		return !filter.accept(member);
	}
}
