package org.kalibro.core.util.reflection;

import java.lang.reflect.Member;
import java.util.regex.Pattern;

final class NameMemberFilter implements MemberFilter {

	private Pattern pattern;

	protected NameMemberFilter(String regularExpression) {
		pattern = Pattern.compile(regularExpression);
	}

	@Override
	public boolean accept(Member member) {
		return pattern.matcher(member.getName()).matches();
	}
}