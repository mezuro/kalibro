package org.kalibro.core.abstractentity;

import static org.kalibro.core.util.reflection.MemberFilterFactory.hasAnnotation;

import java.lang.reflect.AccessibleObject;
import java.util.*;

import org.kalibro.core.util.reflection.MemberFilterAdapter;
import org.kalibro.core.util.reflection.Reflector;

/**
 * Specialized {@link Reflector} for entities.
 * 
 * @author Carlos Morais
 */
class EntityReflector extends Reflector {

	protected EntityReflector(AbstractEntity<?> entity) {
		super(entity, hasAnnotation(Ignore.class));
	}

	protected List<String> listIdentityFields() {
		List<String> identityFields = listFields(hasAnnotation(IdentityField.class));
		return identityFields.isEmpty() ? listFields() : identityFields;
	}

	protected List<String> listSortingFields() {
		return findSortingFields(getObjectClass());
	}

	private List<String> findSortingFields(Class<?> type) {
		if (type == null)
			return new ArrayList<String>();
		if (type.isAnnotationPresent(SortingFields.class))
			return Arrays.asList(type.getAnnotation(SortingFields.class).value());
		return findSortingFields(type.getSuperclass());
	}

	protected List<String> listPrintFields() {
		PrintSelector selector = new PrintSelector();
		List<String> fields = listFields(selector);
		Collections.sort(fields, selector);
		return fields;
	}

	protected String getPrintComment(String field) {
		Print print = getFieldAnnotation(field, Print.class);
		if (print == null)
			return "";
		return print.comment();
	}

	private class PrintSelector extends MemberFilterAdapter implements Comparator<String> {

		@Override
		protected boolean accept(AccessibleObject member) {
			Print print = member.getAnnotation(Print.class);
			return print == null || !print.skip();
		}

		@Override
		public int compare(String field1, String field2) {
			return getOrder(field1).compareTo(getOrder(field2));
		}

		private Integer getOrder(String field) {
			Print print = getFieldAnnotation(field, Print.class);
			return print == null ? Integer.MAX_VALUE : print.order();
		}
	}
}