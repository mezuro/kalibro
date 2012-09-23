package org.kalibro.core.reflection;

import java.lang.reflect.Member;
import java.util.regex.Pattern;

/**
 * This filter accepts members whose name matches the specified regular expression.
 * 
 * @author Carlos Morais
 */
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