package org.kalibro.core.abstractentity;

/**
 * Printer for doubles. Prints infinity doubles correctly.
 * 
 * @author Carlos Morais
 */
class DoublePrinter extends Printer<Double> {

	@Override boolean canPrint(Object object) {
		return object instanceof Double;
	}

	@Override void doPrint(Double value, String comment) {
		printString(" " + convert(value));
		printComment(comment);
	}

	private String convert(Double value) {
		if (value == Double.POSITIVE_INFINITY)
			return ".inf";
		if (value == Double.NEGATIVE_INFINITY)
			return "-.inf";
		return "" + value;
	}
}