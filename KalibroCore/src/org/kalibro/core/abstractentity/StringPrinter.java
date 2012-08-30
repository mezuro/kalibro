package org.kalibro.core.abstractentity;


class StringPrinter extends Printer<String> {

	@Override
	protected boolean canPrint(Object object) {
		return object instanceof String;
	}

	@Override
	protected void doPrint(String string, String comment) {
		printString(" \"" + string.replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"") + "\"");
		printComment(comment);
	}
}