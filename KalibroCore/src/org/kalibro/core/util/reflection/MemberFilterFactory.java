package org.kalibro.core.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

public final class MemberFilterFactory {

	public static MemberFilter hasAnnotation(Class<? extends Annotation> annotationClass) {
		return new AnnotationMemberFilter(annotationClass);
	}

	public static MemberFilter isStatic() {
		return new ModifierMemberFilter(Modifier.STATIC);
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