package org.kalibro.service.xml;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Random;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.MetricConfiguration;
import org.kalibro.MetricResult;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class DateMetricResultXmlTest extends UnitTest {

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<DateMetricResultXml> constructor = DateMetricResultXml.class.getConstructor();
		assertTrue(Modifier.isPublic(constructor.getModifiers()));
	}

	@Test
	public void shouldConvertProperly() {
		Date date = new Date(new Random().nextLong());
		MetricResult metricResult = createMetricResult();
		DateMetricResultXml xml = new DateMetricResultXml(date, metricResult);

		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(MetricResultDao.class, "descendantResultsOf", metricResult.getId()))
			.thenReturn(metricResult.getDescendantResults());

		assertSame(date, xml.date());
		assertDeepEquals(metricResult, xml.metricResult());
	}

	private MetricResult createMetricResult() {
		MetricConfiguration configuration = new MetricConfiguration();
		MetricResult metricResult = new MetricResult(configuration, new Throwable());
		metricResult.setDescendantResults(list(42.0));
		return metricResult;
	}

	@Test
	public void shouldHaveClassAnnotations() {
		Class<DateMetricResultXml> xmlClass = DateMetricResultXml.class;
		assertEquals("dateMetricResult", xmlClass.getAnnotation(XmlRootElement.class).name());
		assertEquals(XmlAccessType.FIELD, xmlClass.getAnnotation(XmlAccessorType.class).value());
	}

	@Test
	public void shouldHaveFieldAnnotations() {
		FieldReflector reflector = new FieldReflector(new DateMetricResultXml());
		assertNotNull(reflector.getFieldAnnotation("date", XmlElement.class));
		assertNotNull(reflector.getFieldAnnotation("metricResult", XmlElement.class));
	}
}