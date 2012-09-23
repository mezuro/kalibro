package org.kalibro.core.abstractentity;

import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Printer.class)
abstract class PrinterTestCase<T> extends UnitTest {

	protected Printer<T> printer = createPrinter();

	protected abstract Printer<T> createPrinter();

	protected String print(Object object, String comment) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Whitebox.invokeMethod(Printer.class, "print", printer, object, buffer, 0, comment);
		return buffer.toString();
	}
}