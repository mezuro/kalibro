package org.kalibro;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ModuleResultTest extends UnitTest {

	private Module module;
	private ModuleResult parent, result;

	@Before
	public void setUp() {
		parent = mock(ModuleResult.class);
		module = new Module(Granularity.CLASS, "ModuleResult");
		result = new ModuleResult(parent, module);
	}

	@Test
	public void checkConstruction() {
		assertNull(result.getId());
		assertSame(module, result.getModule());
		assertDoubleEquals(Double.NaN, result.getGrade());
		assertEquals(0, result.getHeight().intValue());
		assertTrue(result.hasParent());
		assertSame(parent, result.getParent());
		assertTrue(result.getChildren().isEmpty());

		result = new ModuleResult();
		assertNull(result.getModule());
		assertFalse(result.hasParent());
	}

	@Test
	public void shouldSetParentOnGettingChildren() {
		ModuleResult child = new ModuleResult(null, new Module(Granularity.METHOD, "getParent"));
		result.setChildren(sortedSet(child));
		assertDeepEquals(set(child), result.getChildren());
		assertSame(result, child.getParent());
	}

	@Test
	public void shouldSetChildrenWithoutTouchingThem() {
		// required for lazy loading
		SortedSet<ModuleResult> children = mock(SortedSet.class);
		result.setChildren(children);
		verifyZeroInteractions(children);
	}

	@Test
	public void shouldSetParentOnAddChild() {
		ModuleResult child = new ModuleResult(null, new Module(Granularity.METHOD, "getParent"));
		result.addChild(child);
		assertDeepEquals(set(child), result.getChildren());
		assertSame(result, child.getParent());
	}

	@Test
	public void shouldGetHistory() {
		ModuleResultDao dao = mock(ModuleResultDao.class);
		SortedMap<Date, ModuleResult> history = mock(SortedMap.class);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getModuleResultDao()).thenReturn(dao);
		when(dao.historyOf(result.getId())).thenReturn(history);

		assertSame(history, result.history());
	}

	@Test
	public void shouldGetHistoryOfMetric() {
		Metric metric = new CompoundMetric();
		MetricResultDao dao = mock(MetricResultDao.class);
		SortedMap<Date, MetricResult> history = mock(SortedMap.class);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getMetricResultDao()).thenReturn(dao);
		when(dao.historyOf(metric.getName(), result.getId())).thenReturn(history);

		assertSame(history, result.historyOf(metric));
	}

	@Test
	public void toStringShouldPrintModule() {
		assertEquals(result.getModule().toString(), "" + result);
	}
}