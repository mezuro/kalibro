package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class GranularityTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void testToString() {
		assertEquals("Application", "" + APPLICATION);
		assertEquals("Package", "" + PACKAGE);
		assertEquals("Class", "" + CLASS);
		assertEquals("Method", "" + METHOD);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSorting() {
		assertSorted(APPLICATION, PACKAGE, CLASS, METHOD);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInferredParentGranularity() {
		assertEquals(CLASS, METHOD.inferParentGranularity());
		assertEquals(PACKAGE, CLASS.inferParentGranularity());
		assertEquals(PACKAGE, PACKAGE.inferParentGranularity());
		assertEquals(APPLICATION, APPLICATION.inferParentGranularity());
	}
}