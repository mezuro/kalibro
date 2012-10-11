package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;

public class MetricResultXmlTest extends XmlTest {

	@Override
	protected MetricResult loadFixture() {
		MetricConfiguration configuration = new MetricConfiguration();
		MetricResult metricResult = new MetricResult(configuration, new Throwable());
		metricResult.addDescendentResult(42.0);
		return metricResult;
	}

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