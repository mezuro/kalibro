package org.kalibro.service.xml;

import org.kalibro.core.model.Range;
import org.kalibro.core.model.RangeFixtures;
import org.kalibro.core.model.RangeLabel;

public class RangeXmlTest extends XmlTest<Range> {

	@Override
	protected Range loadFixture() {
		return RangeFixtures.newRange("amloc", RangeLabel.GOOD);
	}
}