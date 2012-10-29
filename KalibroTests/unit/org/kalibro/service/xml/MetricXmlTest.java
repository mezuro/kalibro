package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.CompoundMetric;
import org.kalibro.Granularity;
import org.kalibro.Metric;

public class MetricXmlTest extends XmlTest {

	@Override
	protected Class<?> entityClass() {
		return Metric.class;
	}

	@Override
	protected void verifyElements() {
		assertElement("compound", boolean.class, true);
		assertElement("name", String.class, true);
		assertElement("scope", Granularity.class, true);
		assertElement("description", String.class);
		assertElement("script", String.class);
		assertCollection("language");
	}

	@Test
	public void shouldConvertScriptOfCompoundMetric() {
		CompoundMetric metric = loadFixture("sc", CompoundMetric.class);
		assertEquals(metric.getScript(), new MetricXml(metric).script());
	}

	@Test
	public void shouldConvertNullLanguagesIntoEmptyCollection() {
		assertTrue(new MetricXml().languages().isEmpty());
	}
}