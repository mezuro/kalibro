package org.kalibro.core.abstractentity;

/**
 * Printer for strings. Quotes the string before printing.
 * 
 * @author Carlos Morais
 */
class StringPrinter extends Printer<String> {

	@Override
	boolean canPrint(Object object) {
		return object instanceof String;
	}

	@Override
	void doPrint(String string, String comment) {
		printString(" \"" + string.replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"") + "\"");
		printComment(comment);
	}
}