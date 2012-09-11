package org.kalibro.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 * This filter accepts members with the specified annotation.
 * 
 * @author Carlos Morais
 */
final class AnnotationMemberFilter extends MemberFilterAdapter {

	private Class<? extends Annotation> annotationClass;

	protected AnnotationMemberFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	@Override
	protected boolean accept(AccessibleObject member) {
		return member.isAnnotationPresent(annotationClass);
	}
}