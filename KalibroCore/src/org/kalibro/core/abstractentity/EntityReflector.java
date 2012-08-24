package org.kalibro.core.abstractentity;

import static org.kalibro.core.util.reflection.MemberFilterFactory.hasAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}