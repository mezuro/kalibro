package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MetricResultXmlTest extends XmlTest {

	@Override
	public void verifyElements() {
		assertElement("configuration", MetricConfigurationXmlResponse.class);
		assertElement("value", Double.class);
		assertElement("error", ThrowableXml.class);
		assertCollection("descendentResult");
	}

	@Test
	public void shouldConvertNullDescendentResultsIntoEmptyCollection() {
		assertTrue(new MetricResultXml().descendentResults().isEmpty());
	}
}