package org.kalibro.core.abstractentity;

/**
 * Generic printer.
 * 
 * @author Carlos Morais
 */
abstract class Printer<T> {

	protected static String print(Object object) {
		StringBuffer buffer = new StringBuffer("---");
		print(object, buffer, 0, "");
		return buffer.toString();
	}

	protected static void print(Object object, StringBuffer buffer, int indent, String comment) {
		for (Printer<?> printer : specialPrinters())
			if (printer.canPrint(object)) {
				print(printer, object, buffer, indent, comment);
				return;
			}
		print(new ObjectPrinter(), object, buffer, indent, comment);
	}

	private static Printer<?>[] specialPrinters() {
		return new Printer[]{new EntityPrinter(), new EnumPrinter(), new MapPrinter()};
	}

	private static <T> void print(Printer<T> printer, Object object, StringBuffer buffer, int indent, String comment) {
		printer.buffer = buffer;
		printer.indent = indent;
		printer.doPrint((T) object, comment);
	}

	private int indent;
	private StringBuffer buffer;

	protected abstract boolean canPrint(Object object);

	protected abstract void doPrint(T object, String comment);

	protected void printComment(String comment) {
		if (!comment.isEmpty())
			printString(" # " + comment);
	}

	protected void printString(String string) {
		buffer.append(string);
	}

	protected void newLine() {
		buffer.append("\n");
		for (int i = 0; i < indent; i++)
			buffer.append("  ");
	}

	protected void printSubItem(Object subItem) {
		printSubItem(subItem, "");
	}

	protected void printSubItem(Object subItem, String comment) {
		print(subItem, buffer, indent + 1, comment);
	}
}