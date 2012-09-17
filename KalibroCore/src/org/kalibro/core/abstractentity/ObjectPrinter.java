package org.kalibro.core.abstractentity;

/**
 * The default printer. Uses {@code Object.toString()}.
 * 
 * @author Carlos Morais
 */
class ObjectPrinter extends Printer<Object> {

	@Override
	protected boolean canPrint(Object object) {
		return true;
	}

	@Override
	protected void doPrint(Object object, String comment) {
		printString(" " + object);
		printComment(comment);
	}
}