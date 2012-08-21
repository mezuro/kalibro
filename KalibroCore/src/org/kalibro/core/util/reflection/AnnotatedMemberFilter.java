package org.kalibro.core.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 * This filter accepts members with the specified annotation.
 * 
 * @author Carlos Morais
 */
final class AnnotatedMemberFilter extends MemberFilterAdapter {

	private Class<? extends Annotation> annotationClass;

	protected AnnotatedMemberFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	@Override
	protected boolean accept(AccessibleObject member) {
		return member.isAnnotationPresent(annotationClass);
	}
}