package org.kalibro.core.abstractentity;

/**
 * Printer for enum types. Uses {@code Enum.name()}.
 * 
 * @author Carlos Morais
 */
class EnumPrinter extends Printer<Enum<?>> {

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof Enum<?>;
	}

	@Override
	protected void doPrint(Enum<?> object, String comment) {
		printString(" " + object.name());
		printComment(comment);
	}
}