package org.kalibro.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class StackTracePrinterTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkPrintedStackTrace() throws IOException {
		StackTracePrinter printer = new StackTracePrinter(new Exception("My message"));
		String expected = IOUtils.toString(getClass().getResourceAsStream("stackTrace.txt"));
		assertEquals(expected, printer.printStackTrace());
	}
}