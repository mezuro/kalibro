package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;

/**
 * This filter produces a logical NOT of the filter specified.
 * 
 * @author Carlos Morais
 */
final class NotMemberFilter implements MemberFilter {

	private MemberFilter filter;

	protected NotMemberFilter(MemberFilter filter) {
		this.filter = filter;
	}

	@Override
	public boolean accept(Member member) {
		return !filter.accept(member);
	}
}