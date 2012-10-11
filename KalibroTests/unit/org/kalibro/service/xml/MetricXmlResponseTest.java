package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Granularity;
import org.kalibro.Metric;

public class MetricXmlResponseTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return Metric.class;
	}

	@Override
	protected void verifyElements() {
		assertElement("compound", boolean.class);
		assertElement("name", String.class);
		assertElement("scope", Granularity.class);
		assertElement("description", String.class);
		assertElement("script", String.class);
		assertCollection("language");
	}

	@Test
	public void shouldConvertScriptOfCompoundMetric() {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		assertEquals(metric.getScript(), new MetricXmlResponse(metric).script());
	}

	@Test
	public void shouldConvertNullLanguagesIntoEmptyCollection() {
		assertTrue(new MetricXmlResponse().languages().isEmpty());
	}
}