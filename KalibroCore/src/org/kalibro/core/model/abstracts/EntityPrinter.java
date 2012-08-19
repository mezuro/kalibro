package org.kalibro.core.model.abstracts;

import java.util.Comparator;
import java.util.Map;

final class EntityPrinter {

	protected static String print(AbstractEntity<?> entity) {
		StringBuffer buffer = new StringBuffer("---");
		new EntityPrinter(entity, buffer, 0).print();
		return buffer.toString();
	}

	private int indentLevel;
	private StringBuffer buffer;
	private EntityReflector reflector;

	private EntityPrinter(AbstractEntity<?> entity, StringBuffer buffer, int indentLevel) {
		this.buffer = buffer;
		this.indentLevel = indentLevel;
		reflector = new EntityReflector(entity);
	}

	private void print() {
		for (String field : reflector.sortFields(new PrintOrderComparator()))
			printField(field);
	}

	private void printField(String field) {
		printNewLine();
		buffer.append(field + ": ");
		printValue(reflector.get(field));
		Print print = reflector.getFieldAnnotation(field, Print.class);
		if (print != null)
			buffer.append(" # " + print.comment());
	}

	private void printValue(Object value) {
		if (value instanceof AbstractEntity<?>)
			printEntity((AbstractEntity<?>) value);
		else if (value instanceof Map<?, ?>)
			printMap((Map<?, ?>) value);
		else
			buffer.append(value);
	}

	private void printEntity(AbstractEntity<?> entity) {
		new EntityPrinter(entity, buffer, indentLevel + 1).print();
	}

	private void printMap(Map<?, ?> map) {
		if (map.isEmpty()) {
			buffer.append("{}");
			return;
		}
		indentLevel++;
		for (Object key : map.keySet()) {
			printNewLine();
			printValue(key);
			buffer.append(": ");
			printValue(map.get(key));
		}
		indentLevel--;
	}

	private void printNewLine() {
		buffer.append("\n");
		for (int i = 0; i < indentLevel; i++)
			buffer.append("  ");
	}

	private class PrintOrderComparator implements Comparator<String> {

		@Override
		public int compare(String field1, String field2) {
			return getOrder(field1).compareTo(getOrder(field2));
		}

		private Integer getOrder(String field) {
			Print print = reflector.getFieldAnnotation(field, Print.class);
			return print == null ? Integer.MAX_VALUE : print.order();
		}
	}
}