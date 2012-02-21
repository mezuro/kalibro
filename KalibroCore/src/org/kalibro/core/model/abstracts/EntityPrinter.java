package org.kalibro.core.model.abstracts;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EntityPrinter {

	private EntityReflector reflector;

	public EntityPrinter(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	public String simplePrint() {
		return print(reflector.getIdentityFields(), new SimpleFieldPrinter());
	}

	private class SimpleFieldPrinter implements FieldPrinter {

		@Override
		public String print(AbstractEntity<?> fieldValue) {
			return new EntityPrinter(fieldValue).simplePrint();
		}
	}

	public String deepPrint() {
		return print(reflector.getAllFields(), new DeepFieldPrinter());
	}

	private class DeepFieldPrinter implements FieldPrinter {

		@Override
		public String print(AbstractEntity<?> fieldValue) {
			return new EntityPrinter(fieldValue).deepPrint();
		}
	}

	private String print(List<String> fields, FieldPrinter fieldPrinter) {
		String className = reflector.getEntityClass().getSimpleName();
		String string = "$" + className + "(" + printField(fields.get(0), fieldPrinter);
		for (String field : fields.subList(1, fields.size()))
			string += ", " + printField(field, fieldPrinter);
		return string + ")";
	}

	private String printField(String field, FieldPrinter fieldPrinter) {
		return field + " = " + printValue(reflector.get(field), fieldPrinter);
	}

	public String printValue(Object value, FieldPrinter fieldPrinter) {
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
}