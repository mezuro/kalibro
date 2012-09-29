package org.kalibro.core.abstractentity;

/**
 * Printer for entities.
 * 
 * @author Carlos Morais
 */
class EntityPrinter extends Printer<AbstractEntity<?>> {

	private EntityReflector reflector;

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof AbstractEntity<?>;
	}

	@Override
	protected void doPrint(AbstractEntity<?> object, String comment) {
		printString(" !!" + object.getClass().getName());
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