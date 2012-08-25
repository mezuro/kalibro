package org.kalibro.core.abstractentity;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Printer.class)
public class PrinterTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkKnownPrinters() throws Exception {
		Printer<?>[] printers = Whitebox.invokeMethod(Printer.class, "knownPrinters");
		assertEquals(1, printers.length);
		assertClassEquals(ObjectPrinter.class, printers[0]);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkPrint() throws Exception {
		spy(Printer.class);
		doReturn(new Printer[]{new CascadePrinter()}).when(Printer.class, "knownPrinters");
		String expected = loadResource("printer.test");
		assertEquals(expected, Printer.print("Carlos Morais de Oliveira Filho".split("\\s+")));
	}

	private class CascadePrinter extends Printer<String[]> {

		@Override
		protected boolean canPrint(Object object) {
			return object instanceof String[];
		}

		@Override
		protected void doPrint(String[] array, String comment) {
			if (array.length == 0) {
				printComment("FIM");
				return;
			}
			newLine();
			printString(array[0]);
			printSubItem(Arrays.copyOfRange(array, 1, array.length));
		}
	}
}