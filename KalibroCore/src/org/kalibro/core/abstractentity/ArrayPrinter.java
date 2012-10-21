package org.kalibro.core.abstractentity;

/**
 * Printer for arrays.
 * 
 * @author Carlos Morais
 */
class ArrayPrinter extends Printer<Object[]> {

	@Override
	protected boolean canPrint(Object value) {
		return value instanceof Object[];
	}

	@Override
	protected void doPrint(Object[] array, String comment) {
		if (array.length == 0) {
			printString(" []");
			printComment(comment);
		} else
			printArray(array, comment);

	}

	private void printArray(Object[] array, String comment) {
		printComment(comment);
		for (Object element : array) {
			newLine();
			printString("-");
			printSubItem(element);
		}
	}
}