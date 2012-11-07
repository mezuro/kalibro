package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Range;

public class RangeSnapshotXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("beginning", Double.class);
		assertElement("end", Double.class);
		assertElement("label", String.class);
		assertElement("grade", Double.class);
		assertElement("color", String.class);
		assertElement("comments", String.class);
	}

	@Test
	public void shouldReturnAnIdIfHasReading() {
		assertEquals(0L, ((RangeSnapshotXml) dto).readingId().longValue());
	}

	@Test
	public void shouldConstructWithoutReading() {
		assertNull(new RangeSnapshotXml(new Range()).readingId());
	}
}