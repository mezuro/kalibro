package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Range;

public class RangeXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("beginning", Double.class, true);
		assertElement("end", Double.class, true);
		assertElement("comments", String.class);
		assertElement("readingId", Long.class);
	}

	@Test
	public void shouldRetrieveReadingId() {
		Range range = (Range) entity;
		RangeXml xml = (RangeXml) dto;
		assertEquals(range.getReading().getId(), xml.readingId());
	}

	@Test
	public void checkNullReading() {
		assertNull(new RangeXml(new Range()).readingId());
	}
}