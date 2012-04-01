package org.kalibro.core.model.abstracts;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;

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
				putField(field);
	}

	private boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	private void putField(Field field) {
		field.setAccessible(true);
		fields.put(field.getName(), field);
	}

	protected AbstractEntity<?> getEntity() {
		return entity;
	}

	protected Class<?> getEntityClass() {
		return entity.getClass();
	}

	protected List<String> listAllFields() {
		return new ArrayList<String>(fields.keySet());
	}

	protected List<String> listIdentityFields() {
		List<String> identityFields = new ArrayList<String>();
		for (Field field : fields.values())
			if (field.isAnnotationPresent(IdentityField.class))
				identityFields.add(field.getName());
		return identityFields.isEmpty() ? listAllFields() : identityFields;
	}

	protected Object get(String fieldName) {
		String completeFieldName = "Field " + getEntityClass().getName() + "." + fieldName;
		if (!fields.containsKey(fieldName))
			throw new KalibroError(completeFieldName + " does not exist");
		try {
			return fields.get(fieldName).get(entity);
		} catch (IllegalAccessException exception) {
			throw new KalibroError(completeFieldName + " inaccessible", exception);
		}
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

	protected Object invoke(Method method) {
		String methodName = getEntityClass().getName() + "." + method.getName();
		try {
			return method.invoke(entity);
		} catch (IllegalAccessException exception) {
			throw new KalibroError("Method " + methodName + " inaccessible", exception);
		} catch (InvocationTargetException exception) {
			Throwable targetException = exception.getTargetException();
			if (targetException instanceof KalibroException)
				throw (KalibroException) targetException;
			throw new KalibroException("Method " + methodName + " threw exception", targetException);
		}
	}
}