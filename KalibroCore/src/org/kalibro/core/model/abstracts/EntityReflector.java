package org.kalibro.core.model.abstracts;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class EntityReflector {

	private AbstractEntity<?> entity;
	private Map<String, Field> fields;

	protected EntityReflector(AbstractEntity<?> entity) {
		this.entity = entity;
		initializeFields();
	}

	private void initializeFields() {
		fields = new TreeMap<String, Field>();
		putAllFields(entity.getClass());
	}

	private void putAllFields(Class<?> type) {
		if (!type.equals(Object.class)) {
			putDeclaredFields(type);
			putAllFields(type.getSuperclass());
		}
	}

	private void putDeclaredFields(Class<?> type) {
		for (Field field : type.getDeclaredFields())
			if (!isStatic(field))
				fields.put(field.getName(), field);
	}

	private boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	protected AbstractEntity<?> getEntity() {
		return entity;
	}

	protected Class<?> getEntityClass() {
		return entity.getClass();
	}

	protected List<String> getAllFields() {
		return new ArrayList<String>(fields.keySet());
	}

	protected List<String> getIdentityFields() {
		List<String> identityFields = new ArrayList<String>();
		for (Field field : fields.values())
			if (field.isAnnotationPresent(IdentityField.class))
				identityFields.add(field.getName());
		return identityFields.isEmpty() ? getAllFields() : identityFields;
	}

	protected Object get(String fieldName) {
		try {
			Field field = fields.get(fieldName);
			field.setAccessible(true);
			return field.get(entity);
		} catch (Exception exception) {
			String errorMessage = "Field " + entity.getClass().getName() + "." + fieldName + " does not exist";
			throw new IllegalArgumentException(errorMessage, exception);
		}
	}

	protected Object invoke(Method method) {
		try {
			return method.invoke(entity);
		} catch (Exception exception) {
			throw new RuntimeException("Error invoking method", exception);
		}
	}

	protected List<Method> getSortingMethods() {
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

	private Method getSortingMethod(Class<?> type, String fieldName) {
		try {
			return type.getMethod(fieldName);
		} catch (NoSuchMethodException exception) {
			// TODO create Kalibro exception(s) instead of using RuntimeException
			throw new RuntimeException("Method not found", exception);
		}
	}
}