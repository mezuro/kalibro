package org.kalibro.core.abstractentity;

import java.util.Collection;

/**
 * Printer for collections.
 * 
 * @author Carlos Morais
 */
class CollectionPrinter extends Printer<Collection<?>> {

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof Collection;
	}

	@Override
	protected void doPrint(Collection<?> collection, String comment) {
		if (collection.isEmpty()) {
			printString(" []");
			printComment(comment);
		} else
			printCollection(collection, comment);

	}

	private void printCollection(Collection<?> collection, String comment) {
		printComment(comment);
		for (Object element : collection) {
			newLine();
			printString("-");
			printSubItem(element);
		}
	}
}