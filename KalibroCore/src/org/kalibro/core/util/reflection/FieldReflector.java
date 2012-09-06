package org.kalibro.core.util.reflection;

import static java.lang.reflect.Modifier.STATIC;
import static org.kalibro.core.util.reflection.MemberFilterFactory.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kalibro.KalibroError;

/**
 * Performs reflective operations on the specified object fields in an easy way.
 * 
 * @author Carlos Morais
 */
public class FieldReflector {

	private Object object;
	private Map<String, Field> fields;
	private MemberFilter ignoreFieldFilter;

	public FieldReflector(Object object, MemberFilter... ignoreFieldFilters) {
		this.object = object;
		this.ignoreFieldFilter = or(is(STATIC), nameMatches(".*\\$.*"), or(ignoreFieldFilters));
		initializeFields();
	}

	private void initializeFields() {
		fields = new TreeMap<String, Field>();
		putAllFields(getObjectClass());
	}

	private void putAllFields(Class<?> type) {
		if (!type.equals(Object.class)) {
			putDeclaredFields(type);
			putAllFields(type.getSuperclass());
		}
	}

	private void putDeclaredFields(Class<?> type) {
		for (Field field : type.getDeclaredFields())
			if (!ignoreFieldFilter.accept(field)) {
				field.setAccessible(true);
				fields.put(field.getName(), field);
			}
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

	public <T extends Annotation> T getFieldAnnotation(String field, Class<T> annotationClass) {
		return fields.get(field).getAnnotation(annotationClass);
	}
}