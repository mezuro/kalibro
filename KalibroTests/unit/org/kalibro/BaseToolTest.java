package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.MetricCollectorStub.*;

import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class BaseToolTest extends UnitTest {

	private BaseToolDao dao;

	private BaseTool baseTool;

	@Before
	public void setUp() {
		baseTool = new BaseTool(CLASS_NAME);
		dao = mock(BaseToolDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(dao);
	}

	@Test
	public void shoouldGetAllBaseTools() {
		SortedSet<String> baseToolNames = mock(SortedSet.class);
		when(dao.allNames()).thenReturn(baseToolNames);
		assertSame(baseToolNames, BaseTool.allNames());
	}

	@Test
	public void shoouldGetBaseToolByName() {
		String name = mock(String.class);
		when(dao.get(name)).thenReturn(baseTool);
		assertSame(baseTool, BaseTool.get(name));
	}

	@Test
	public void shouldSortByName() {
		assertSorted(withName("A"), withName("B"), withName("C"), withName("X"), withName("Y"), withName("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(baseTool, withName(baseTool.getName()));
	}

	private BaseTool withName(String name) {
		return new BaseTool(name, null, null, null);
	}

	@Test
	public void shouldHaveDefaultConstructorForFrameworks() {
		assertDeepEquals(new BaseTool(), new BaseTool(null, null, null, null));
	}

	@Test
	public void checkConstruction() {
		check();
		baseTool = new BaseTool(NAME, DESCRIPTION, asSet(SUPPORTED_METRIC), CLASS_NAME);
		check();
	}

	private void check() {
		assertEquals(NAME, baseTool.getName());
		assertEquals(DESCRIPTION, baseTool.getDescription());
		assertEquals(asSet(SUPPORTED_METRIC), baseTool.getSupportedMetrics());
		assertEquals(CLASS_NAME, baseTool.getCollectorClassName());
	}

	@Test
	public void shouldGetSupportedMetricByName() {
		assertSame(SUPPORTED_METRIC, baseTool.getSupportedMetric(SUPPORTED_METRIC.getName()));
		assertThat(new VoidTask() {

			@Override
			protected void perform() throws Throwable {
				baseTool.getSupportedMetric("inexistent");
			}
		}).throwsException().withMessage("Base tool '" + NAME + "' does not support metric: inexistent");
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		assertEquals(asSet(RESULT), baseTool.collectMetrics(null, null));
	}

	@Test
	public void shouldThrowExceptionIfCannotCreateCollector() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new BaseTool("invalid.Class");
			}
		}).throwsException().withMessage("Could not create metric collector: invalid.Class")
			.withCause(ClassNotFoundException.class);
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(baseTool.getName(), "" + baseTool);
	}
}