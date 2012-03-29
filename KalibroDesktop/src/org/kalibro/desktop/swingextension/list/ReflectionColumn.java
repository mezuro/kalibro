package org.kalibro.desktop.swingextension.list;

import java.lang.reflect.Method;

import org.kalibro.KalibroException;
import org.kalibro.core.util.Identifier;
import org.kalibro.desktop.swingextension.renderer.DefaultRenderer;
import org.kalibro.desktop.swingextension.renderer.TableRenderer;

public class ReflectionColumn extends Column {

	private String[] attributes;

	public ReflectionColumn(String path, int width) {
		this(path, width, new DefaultRenderer());
	}

	public ReflectionColumn(String path, int width, TableRenderer renderer) {
		super(null, null, width, renderer);
		this.attributes = path.split("\\.");
	}

	@Override
	public String getTitle() {
		String lastAttribute = attributes[attributes.length - 1];
		return Identifier.fromVariable(lastAttribute).asText();
	}

	protected Class<?> getColumnClass(Class<?> dataClass) {
		Class<?> columnClass = dataClass;
		for (String attribute : attributes)
			columnClass = getMethod(columnClass, attribute).getReturnType();
		return columnClass;
	}

	public Object getValue(Object object) {
		try {
			Object value = object;
			for (String attribute : attributes)
				value = getMethod(value.getClass(), attribute).invoke(value);
			return value;
		} catch (Exception exception) {
			return null;
		}
	}

	private Method getMethod(Class<?> entityClass, String attribute) {
		String capitalized = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
		String methodName = Identifier.fromText("get " + capitalized).asVariable();
		try {
			return entityClass.getMethod(methodName);
		} catch (NoSuchMethodException exception) {
			String completeMethodName = entityClass.getCanonicalName() + "." + methodName;
			throw new KalibroException("Reflection column did not found method: " + completeMethodName, exception);
		}
	}
}