package org.kalibro.service.xml;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MetricResultXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("configuration", MetricConfigurationSnapshotXml.class);
		assertElement("value", Double.class);
		assertElement("error", ThrowableXml.class);
	}

	@Test
	public void checkNullError() {
		assertNull(new MetricResultXml().error());
	}
}