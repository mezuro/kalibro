package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetricResultXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("configuration", MetricConfigurationSnapshotXml.class);
		assertElement("value", Double.class);
		assertElement("error", ThrowableXml.class);
		assertCollection("descendentResult");
	}

	@Test
	public void shouldConvertNullDescendentResultsIntoEmptyCollection() {
		assertTrue(new MetricResultXml().descendentResults().isEmpty());
	}

	@Test
	public void checkNullError() {
		assertNull(new MetricResultXml().error());
	}
}