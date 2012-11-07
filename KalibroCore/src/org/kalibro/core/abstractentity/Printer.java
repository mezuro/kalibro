package org.kalibro.core.abstractentity;

/**
 * Generic printer.
 * 
 * @author Carlos Morais
 */
public abstract class Printer<T> {

	public static synchronized String print(Object object) {
		EntityPrinter.PRINTED.clear();
		StringBuffer buffer = new StringBuffer();
		print(object, buffer, 0, "");
		return buffer.toString();
	}

	private static void print(Object object, StringBuffer buffer, int indent, String comment) {
		for (Printer<?> printer : specialPrinters())
			if (printer.canPrint(object)) {
				print(printer, object, buffer, indent, comment);
				return;
			}
		print(new ObjectPrinter(), object, buffer, indent, comment);
	}

	private static Printer<?>[] specialPrinters() {
		return new Printer[]{new ArrayPrinter(), new CollectionPrinter(), new ColorPrinter(), new DoublePrinter(),
			new EntityPrinter(), new EnumPrinter(), new MapPrinter(), new StringPrinter()};
	}

	private static <T> void print(Printer<T> printer, Object object, StringBuffer buffer, int indent, String comment) {
		printer.buffer = buffer;
		printer.indent = indent;
		printer.doPrint((T) object, comment);
	}

	private int indent;
	private StringBuffer buffer;

	abstract boolean canPrint(Object object);

	abstract void doPrint(T object, String comment);

	void printComment(String comment) {
		if (!comment.isEmpty())
			printString(" # " + comment);
	}

	void printString(String string) {
		buffer.append(string);
	}

	void newLine() {
		buffer.append("\n");
		for (int i = 0; i < indent; i++)
			buffer.append("  ");
	}

	void printSubItem(Object subItem) {
		printSubItem(subItem, "");
	}

	void printSubItem(Object subItem, String comment) {
		print(subItem, buffer, indent + 1, comment);
	}
}