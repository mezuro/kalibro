package org.kalibro.service;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.dao.MetricResultDao;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.service.xml.DateModuleResultXml;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({DaoLazyLoader.class, ModuleResultEndpointImpl.class})
public class ModuleResultEndpointImplTest extends
	EndpointImplementorTest<ModuleResult, ModuleResultXml, ModuleResultDao, ModuleResultEndpointImpl> {

	private static final Long ID = new Random().nextLong();

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertSame(xml, implementor.getModuleResult(ID));
	}

	@Test
	public void shouldGetChildren() {
		doReturn(sortedSet(entity)).when(dao).childrenOf(ID);
		assertDeepEquals(list(xml), implementor.childrenOf(ID));
	}

	@Test
	public void shouldGetHistory() {
		mockLazyLoad();

		Date date = new Date(1);
		ModuleResult result = new ModuleResult(null, new Module(Granularity.METHOD, "shouldGetHistory"));
		SortedMap<Date, ModuleResult> map = new TreeMap<Date, ModuleResult>();
		map.put(date, result);

		when(dao.historyOf(ID)).thenReturn(map);
		List<DateModuleResultXml> history = implementor.historyOfModule(ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepEquals(result, history.get(0).moduleResult());
	}

	private void mockLazyLoad() {
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(ModuleResultDao.class, "get", (Long) null)).thenReturn(null);
		when(DaoLazyLoader.createProxy(ModuleResultDao.class, "childrenOf", (Long) null)).thenReturn(sortedSet());
		when(DaoLazyLoader.createProxy(MetricResultDao.class, "metricResultsOf", (Long) null)).thenReturn(sortedSet());
	}
}
