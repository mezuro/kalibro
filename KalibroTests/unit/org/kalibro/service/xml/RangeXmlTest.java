package org.kalibro.service.xml;

import org.kalibro.Range;
import org.kalibro.RangeFixtures;
import org.kalibro.RangeLabel;

public class RangeXmlTest extends XmlTest<Range> {

	@Override
	protected Range loadFixture() {
		return RangeFixtures.newRange("amloc", RangeLabel.GOOD);
	}
}