package org.kalibro.core.model.enums;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Test;
import org.kalibro.EnumerationTestCase;

public class GranularityTest extends EnumerationTestCase<Granularity> {

	@Override
	protected Class<Granularity> enumerationClass() {
		return Granularity.class;
	}

	@Test
	public void testSorting() {
		assertSorted(SOFTWARE, PACKAGE, CLASS, METHOD);
	}

	@Test
	public void checkInferredParentGranularity() {
		assertEquals(CLASS, METHOD.inferParentGranularity());
		assertEquals(PACKAGE, CLASS.inferParentGranularity());
		assertEquals(PACKAGE, PACKAGE.inferParentGranularity());
		assertEquals(SOFTWARE, SOFTWARE.inferParentGranularity());
	}
}