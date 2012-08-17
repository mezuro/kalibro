package org.kalibro.core.model.abstracts;

import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.kalibro.KalibroError;
import org.kalibro.core.util.reflection.Reflector;

class EntityReflector extends Reflector {

	protected EntityReflector(AbstractEntity<?> entity) {
		super(entity);
	}

	@Override
	protected boolean isRelevantField(Field field) {
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		return super.isRelevantField(field) && !isStatic && !field.isAnnotationPresent(Ignore.class);
	}

	protected List<String> listIdentityFields() {
		List<String> identityFields = listFields(hasAnnotation(IdentityField.class));
		return identityFields.isEmpty() ? listFields() : identityFields;
	}

	protected List<Method> listSortingMethods() {
		return findSortingMethods(getObjectClass());
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
}