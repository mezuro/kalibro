package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

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
	public void checkNullReading() {
		assertNull(new RangeSnapshotXml(new Range()).reading());
	}
}