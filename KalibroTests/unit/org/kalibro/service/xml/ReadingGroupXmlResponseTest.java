package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.ReadingGroup;

public class ReadingGroupXmlResponseTest extends XmlTest<ReadingGroup, ReadingGroupXmlResponse> {

	@Override
	protected ReadingGroup loadFixture() {
		return loadFixture("scholar", ReadingGroup.class);
	}

	@Test
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("name", String.class, false);
		assertElement("description", String.class, false);
	}
}