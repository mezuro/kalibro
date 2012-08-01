package org.kalibro.core.util.reflection;

import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;

public class Reflector {

	private Object object;
	private Map<String, Field> fields;
	private Map<String, Method> methods;

	public Reflector(Object object) {
		this.object = object;
		initializeMembers();
	}

	private void initializeMembers() {
		fields = new TreeMap<String, Field>();
		methods = new TreeMap<String, Method>();
		putAllFields(getObjectClass());
		for (Method method : getObjectClass().getMethods())
			if (!method.getDeclaringClass().equals(Object.class))
				methods.put(method.getName(), method);
	}

	private void putAllFields(Class<?> type) {
		if (!type.equals(Object.class)) {
			putDeclaredFields(type);
			putAllFields(type.getSuperclass());
		}
	}

	private void putDeclaredFields(Class<?> type) {
		for (Field field : type.getDeclaredFields())
			if (isRelevantField(field)) {
				field.setAccessible(true);
				fields.put(field.getName(), field);
			}
	}

	private boolean isRelevantField(Field field) {
		String name = field.getName();
		boolean serial = Modifier.isStatic(field.getModifiers()) && name.equals("serialVersionUID");
		boolean dollar = name.startsWith("$");
		return ! (serial || dollar);
	}

	public Object getObject() {
		return object;
	}

	public Class<?> getObjectClass() {
		return object.getClass();
	}

	public List<String> listFields() {
		return new ArrayList<String>(fields.keySet());
	}

	public List<String> listFields(MemberFilter filter) {
		List<String> fieldNames = new ArrayList<String>();
		for (String fieldName : fields.keySet())
			if (filter.accept(fields.get(fieldName)))
				fieldNames.add(fieldName);
		return fieldNames;
	}

	public Object get(String fieldName) {
		String completeFieldName = getObjectClass().getName() + "." + fieldName;
		try {
			return fields.get(fieldName).get(object);
		} catch (Exception exception) {
			throw new KalibroError("Error retrieving field: " + completeFieldName, exception);
		}
	}

	public void set(String fieldName, Object value) {
		String completeFieldName = getObjectClass().getName() + "." + fieldName;
		try {
			fields.get(fieldName).set(object, value);
		} catch (Exception exception) {
			throw new KalibroError("Error setting field: " + completeFieldName, exception);
		}
	}

	public <T extends Annotation> List<T> getFieldAnnotations(Class<T> annotationClass) {
		List<T> annotations = new ArrayList<T>();
		for (String fieldName : listFields(hasAnnotation(annotationClass)))
			annotations.add(fields.get(fieldName).getAnnotation(annotationClass));
		return annotations;
	}

	public List<String> listMethods() {
		return new ArrayList<String>(methods.keySet());
	}

	public List<String> listMethods(MemberFilter filter) {
		List<String> methodNames = new ArrayList<String>();
		for (String methodName : methods.keySet())
			if (filter.accept(methods.get(methodName)))
				methodNames.add(methodName);
		return methodNames;
	}

	public Object invoke(String methodName, Object... arguments) {
		String completeName = getObjectClass().getName() + "." + methodName;
		try {
			return methods.get(methodName).invoke(object, arguments);
		} catch (Exception exception) {
			throw new KalibroException("Error invoking method: " + completeName, exception);
		}
	}
}