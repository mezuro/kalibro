package org.kalibro.core.abstractentity;

import java.util.ArrayList;
import java.util.List;

/**
 * Printer for entities.
 * 
 * @author Carlos Morais
 */
class EntityPrinter extends Printer<AbstractEntity<?>> {

	static final List<AbstractEntity<?>> PRINTED = new ArrayList<AbstractEntity<?>>();

	private EntityReflector reflector;

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof AbstractEntity<?>;
	}

	@Override
	protected void doPrint(AbstractEntity<?> object, String comment) {
		for (int id = 1; id <= PRINTED.size(); id++)
			if (PRINTED.get(id - 1) == object) {
				printString(" *id" + id);
				return;
			}
		PRINTED.add(object);
		printString(" &id" + PRINTED.size() + " !!" + object.getClass().getName());
		printComment(comment);
		reflector = new EntityReflector(object);
		for (String field : reflector.listPrintFields())
			printField(field);
	}

	private void printField(String field) {
		newLine();
		printString(field + ":");
		printSubItem(reflector.get(field), reflector.getPrintComment(field));
	}
}