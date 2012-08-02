package org.kalibro.core.model.abstracts;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class EntityPrinter {

	private EntityReflector reflector;

	protected EntityPrinter(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	protected String simplePrint() {
		return print(reflector.listIdentityFields(), new SimpleFieldPrinter());
	}

	protected String deepPrint() {
		return print(reflector.listFields(), new DeepFieldPrinter());
	}

	private String print(List<String> fields, FieldPrinter fieldPrinter) {
		String className = reflector.getObjectClass().getSimpleName();
		String string = "$" + className + "(" + printField(fields.get(0), fieldPrinter);
		for (String field : fields.subList(1, fields.size()))
			string += ", " + printField(field, fieldPrinter);
		return string + ")";
	}

	private String printField(String field, FieldPrinter fieldPrinter) {
		return field + " = " + printValue(reflector.get(field), fieldPrinter);
	}

	protected String printValue(Object value, FieldPrinter fieldPrinter) {
		if (value instanceof AbstractEntity<?>)
			return fieldPrinter.print((AbstractEntity<?>) value);
		if (value instanceof Collection<?>)
			return printCollection((Collection<?>) value, fieldPrinter);
		if (value instanceof Map<?, ?>)
			return printMap((Map<?, ?>) value, fieldPrinter);
		return "" + value;
	}

	private String printCollection(Collection<?> collection, FieldPrinter fieldPrinter) {
		if (collection.isEmpty())
			return "{}";
		String string = "{";
		for (Object element : collection)
			string += printValue(element, fieldPrinter) + ", ";
		return string.substring(0, string.length() - 2) + "}";
	}

	private String printMap(Map<?, ?> map, FieldPrinter fieldPrinter) {
		if (map.isEmpty())
			return "{}";
		String string = "{";
		for (Object key : map.keySet())
			string += key + " = " + printValue(map.get(key), fieldPrinter) + ", ";
		return string.substring(0, string.length() - 2) + "}";
	}

	private interface FieldPrinter {

		String print(AbstractEntity<?> fieldValue);
	}

	private class SimpleFieldPrinter implements FieldPrinter {

		@Override
		public String print(AbstractEntity<?> fieldValue) {
			return new EntityPrinter(fieldValue).simplePrint();
		}
	}

	private class DeepFieldPrinter implements FieldPrinter {

		@Override
		public String print(AbstractEntity<?> fieldValue) {
			return new EntityPrinter(fieldValue).deepPrint();
		}
	}
}