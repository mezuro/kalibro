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
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class DateModuleResultXmlTest extends UnitTest {

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<DateModuleResultXml> constructor = DateModuleResultXml.class.getConstructor();
		assertTrue(Modifier.isPublic(constructor.getModifiers()));
	}

	@Test
	public void shouldConvertProperly() {
		Date date = new Date(new Random().nextLong());
		ModuleResult moduleResult = createModuleResult();
		DateModuleResultXml xml = new DateModuleResultXml(date, moduleResult);

		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ModuleResultDao.class, "get", moduleResult.getParent().getId()))
			.thenReturn(moduleResult.getParent());
		when(DaoLazyLoader.createProxy(ModuleResultDao.class, "childrenOf", moduleResult.getId()))
			.thenReturn(moduleResult.getChildren());
		when(DaoLazyLoader.createProxy(MetricResultDao.class, "metricResultsOf", moduleResult.getId()))
			.thenReturn(moduleResult.getMetricResults());

		assertSame(date, xml.date());
		assertDeepEquals(moduleResult, xml.moduleResult());
	}

	private ModuleResult createModuleResult() {
		Module module = new Module(Granularity.CLASS, "xml.DateModuleResultXmlTest");
		ModuleResult parent = new ModuleResult(null, module.inferParent().inferParent());
		ModuleResult moduleResult = new ModuleResult(parent, module.inferParent());
		moduleResult.addChild(new ModuleResult(moduleResult, module));
		return moduleResult;
	}

	@Test
	public void shouldHaveClassAnnotations() {
		Class<DateModuleResultXml> xmlClass = DateModuleResultXml.class;
		assertEquals("dateModuleResult", xmlClass.getAnnotation(XmlRootElement.class).name());
		assertEquals(XmlAccessType.FIELD, xmlClass.getAnnotation(XmlAccessorType.class).value());
	}

	@Test
	public void shouldHaveFieldAnnotations() {
		FieldReflector reflector = new FieldReflector(new DateModuleResultXml());
		assertNotNull(reflector.getFieldAnnotation("date", XmlElement.class));
		assertNotNull(reflector.getFieldAnnotation("moduleResult", XmlElement.class));
	}
}