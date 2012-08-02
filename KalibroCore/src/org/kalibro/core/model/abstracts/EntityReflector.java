package org.kalibro.core.model.abstracts;

import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.kalibro.KalibroError;
import org.kalibro.core.util.reflection.Reflector;

class EntityReflector {

	private Reflector reflector;

	protected EntityReflector(AbstractEntity<?> entity) {
		reflector = new Reflector(entity);
	}

	protected AbstractEntity<?> getEntity() {
		return (AbstractEntity<?>) reflector.getObject();
	}

	protected Class<?> getEntityClass() {
		return reflector.getObjectClass();
	}

	protected List<String> listAllFields() {
		return reflector.listFields(not(or(isStatic(), hasAnnotation(Ignore.class))));
	}

	protected List<String> listIdentityFields() {
		List<String> identityFields = reflector.listFields(hasAnnotation(IdentityField.class));
		return identityFields.isEmpty() ? listAllFields() : identityFields;
	}

	protected Object get(String fieldName) {
		return reflector.get(fieldName);
	}

	protected List<Method> listSortingMethods() {
		return findSortingMethods(getEntityClass());
	}

	private List<Method> findSortingMethods(Class<?> type) {
		if (type == null)
			return new ArrayList<Method>();
		if (type.isAnnotationPresent(SortingMethods.class))
			return getSortingMethods(type);
		return findSortingMethods(type.getSuperclass());
	}

	private List<Method> getSortingMethods(Class<?> type) {
		List<Method> sortingMethods = new ArrayList<Method>();
		for (String methodName : type.getAnnotation(SortingMethods.class).value())
			sortingMethods.add(getSortingMethod(type, methodName));
		return sortingMethods;
	}

	private Method getSortingMethod(Class<?> type, String methodName) {
		try {
			return type.getMethod(methodName);
		} catch (NoSuchMethodException exception) {
			throw new KalibroError("Sorting method not found: " + type.getName() + "." + methodName, exception);
		}
	}

	protected Object invoke(String methodName) {
		return reflector.invoke(methodName);
	}
}