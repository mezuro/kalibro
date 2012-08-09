package org.kalibro.core.model.enums;

import static org.junit.Assert.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.EnumerationTestCase;

public class GranularityTest extends EnumerationTestCase<Granularity> {

	@Override
	protected Class<Granularity> enumerationClass() {
		return Granularity.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSorting() {
		assertSorted(SOFTWARE, PACKAGE, CLASS, METHOD);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInferredParentGranularity() {
		assertEquals(CLASS, METHOD.inferParentGranularity());
		assertEquals(PACKAGE, CLASS.inferParentGranularity());
		assertEquals(PACKAGE, PACKAGE.inferParentGranularity());
		assertEquals(SOFTWARE, SOFTWARE.inferParentGranularity());
	}
}