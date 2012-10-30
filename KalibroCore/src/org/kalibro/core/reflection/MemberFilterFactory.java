package org.kalibro.core.reflection;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

/**
 * Allows elegant creation of filters.
 * 
 * @author Carlos Morais
 */
public final class MemberFilterFactory {

	public static MemberFilter is(int modifier) {
		return new ModifierMemberFilter(modifier);
	}

	public static MemberFilter hasType(Class<?> type) {
		return new TypeMemberFilter(type);
	}

	public static MemberFilter hasAnnotation(Class<? extends Annotation> annotationClass) {
		return new AnnotationMemberFilter(annotationClass);
	}

	public static MemberFilter nameMatches(String regularExpression) {
		return new NameMemberFilter(regularExpression);
	}

	public static MemberFilter named(String name) {
		return new NameMemberFilter(Pattern.quote(name));
	}

	public static MemberFilter not(MemberFilter filter) {
		return new NotMemberFilter(filter);
	}

	public static MemberFilter and(MemberFilter... filters) {
		return new AndMemberFilter(filters);
	}

	public static MemberFilter or(MemberFilter... filters) {
		return new OrMemberFilter(filters);
	}

	private MemberFilterFactory() {
		return;
	}
}