package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Printer.class)
public class PrinterTest extends UnitTest {

	@Test
	public void checkSpecialPrinters() throws Exception {
		Printer<?>[] printers = Whitebox.invokeMethod(Printer.class, "specialPrinters");
		assertEquals(8, printers.length);
		assertClassEquals(ArrayPrinter.class, printers[0]);
		assertClassEquals(CollectionPrinter.class, printers[1]);
		assertClassEquals(ColorPrinter.class, printers[2]);
		assertClassEquals(DoublePrinter.class, printers[3]);
		assertClassEquals(EntityPrinter.class, printers[4]);
		assertClassEquals(EnumPrinter.class, printers[5]);
		assertClassEquals(MapPrinter.class, printers[6]);
		assertClassEquals(StringPrinter.class, printers[7]);
	}

	@Test
	public void checkPrint() throws Exception {
		spy(Printer.class);
		doReturn(new Printer[]{new CascadePrinter()}).when(Printer.class, "specialPrinters");
		String expected = loadResource("printer.test");
		assertEquals(expected, Printer.print("Carlos Morais de Oliveira Filho".split("\\s+")));
	}

	private class CascadePrinter extends Printer<String[]> {

		@Override
		boolean canPrint(Object object) {
			return object instanceof String[];
		}

		@Override
		void doPrint(String[] array, String comment) {
			if (array.length == 0) {
				printSubItem("", "FIM");
				return;
			}
			newLine();
			printString(array[0]);
			printSubItem(Arrays.copyOfRange(array, 1, array.length));
		}
	}
}