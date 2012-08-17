package org.kalibro.core.model.abstracts;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class EntityPrinter {

	private EntityReflector reflector;

	protected EntityPrinter(AbstractEntity<?> entity) {
		reflector = new EntityReflector(entity);
	}

	protected String print() {
		return print(reflector.listFields());
	}

	private String print(List<String> fields) {
		String className = reflector.getObjectClass().getSimpleName();
		String string = "$" + className + "(" + printField(fields.get(0));
		for (String field : fields.subList(1, fields.size()))
			string += ", " + printField(field);
		return string + ")";
	}

	private String printField(String field) {
		return field + " = " + printValue(reflector.get(field));
	}

	protected String printValue(Object value) {
		if (value instanceof AbstractEntity<?>)
			return new EntityPrinter((AbstractEntity<?>) value).print();
		if (value instanceof Collection<?>)
			return printCollection((Collection<?>) value);
		if (value instanceof Map<?, ?>)
			return printMap((Map<?, ?>) value);
		return "" + value;
	}

	private String printCollection(Collection<?> collection) {
		if (collection.isEmpty())
			return "{}";
		String string = "{";
		for (Object element : collection)
			string += printValue(element) + ", ";
		return string.substring(0, string.length() - 2) + "}";
	}

	private String printMap(Map<?, ?> map) {
		if (map.isEmpty())
			return "{}";
		String string = "{";
		for (Object key : map.keySet())
			string += key + " = " + printValue(map.get(key)) + ", ";
		return string.substring(0, string.length() - 2) + "}";
	}
}