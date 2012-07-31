package org.kalibro.core.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

final class AnnotatedMemberFilter extends MemberFilterAdapter {

	private Class<? extends Annotation> annotationClass;

	public AnnotatedMemberFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	@Override
	protected boolean accept(AccessibleObject member) {
		return member.isAnnotationPresent(annotationClass);
	}
}