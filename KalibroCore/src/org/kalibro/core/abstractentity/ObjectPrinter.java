package org.kalibro.core.abstractentity;

/**
 * The default printer. Uses {@code Object.toString()}.
 * 
 * @author Carlos Morais
 */
class ObjectPrinter extends Printer<Object> {

	@Override
	boolean canPrint(Object object) {
		return true;
	}

	@Override
	void doPrint(Object object, String comment) {
		printString(" " + object);
		printComment(comment);
	}
}