package org.kalibro.service.xml;

import org.kalibro.Range;

public class RangeXmlResponseTest extends XmlTest<Range> {

	@Override
	protected Range loadFixture() {
		return loadFixture("lcom4-bad", Range.class);
	}
}