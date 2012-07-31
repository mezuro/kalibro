package org.kalibro.core.util.reflection;

import java.lang.annotation.Annotation;

public final class MemberFilterFactory {

	public static MemberFilter isAnnotatedWith(Class<? extends Annotation> annotationClass) {
		return new AnnotatedMemberFilter(annotationClass);
	}

	public static MemberFilter isStatic() {
		return new StaticMemberFilter();
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