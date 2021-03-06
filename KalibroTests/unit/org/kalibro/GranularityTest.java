package org.kalibro;

import static org.junit.Assert.assertEquals;
import static org.kalibro.Granularity.*;

import org.junit.Test;
import org.kalibro.tests.EnumerationTest;

public class GranularityTest extends EnumerationTest<Granularity> {

	@Override
	protected Class<Granularity> enumerationClass() {
		return Granularity.class;
	}

	@Test
	public void shouldInferParentGranularity() {
		assertEquals(CLASS, METHOD.inferParentGranularity());
		assertEquals(PACKAGE, CLASS.inferParentGranularity());
		assertEquals(PACKAGE, PACKAGE.inferParentGranularity());
		assertEquals(SOFTWARE, SOFTWARE.inferParentGranularity());
	}
}