package org.kalibro.service.xml;

import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.dto.DataTransferObject;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataTransferObject.class)
public class ConfigurationXmlRequestTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Set<MetricConfiguration> metricConfigurations = Whitebox.getInternalState(entity, "metricConfigurations");
		spy(DataTransferObject.class);
		doReturn(new TreeSet<MetricConfiguration>(metricConfigurations))
			.when(DataTransferObject.class, "toSortedSet", any());
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertCollection("metricConfiguration");
	}

	@Test
	public void shouldConvertNullMetricConfigurationsIntoEmptyCollection() {
		assertTrue(new ConfigurationXmlRequest().metricConfigurations().isEmpty());
	}
}