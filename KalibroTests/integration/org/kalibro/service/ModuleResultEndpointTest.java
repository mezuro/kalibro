package org.kalibro.service;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.xml.DateModuleResultXml;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.reflect.Whitebox;

public class ModuleResultEndpointTest extends EndpointTest<ModuleResult, ModuleResultDao, ModuleResultEndpoint> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected ModuleResult loadFixture() {
		Module module = new Module(Granularity.PACKAGE, "org", "kalibro", "service");
		ModuleResult moduleResult = new ModuleResult(null, module);
		Whitebox.setInternalState(moduleResult, "id", ID);
		return moduleResult;
	}

	@Override
	protected List<String> fieldsThatShouldBeProxy() {
		return list("children", "metricResults");
	}

	@Test
	public void shouldGetById() {
		when(dao.get(ID)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getModuleResult(ID));
	}

	@Test
	public void shouldGetChildren() {
		when(dao.childrenOf(ID)).thenReturn(sortedSet(entity));
		assertDeepDtoList(list(entity), port.childrenOf(ID));
	}

	@Test
	public void shouldGetModuleResultHistory() {
		Date date = new Date(1);
		SortedMap<Date, ModuleResult> map = new TreeMap<Date, ModuleResult>();
		map.put(date, entity);

		when(dao.historyOf(ID)).thenReturn(map);
		List<DateModuleResultXml> history = port.historyOf(ID);
		assertEquals(1, history.size());
		assertEquals(date, history.get(0).date());
		assertDeepDtoEquals(Whitebox.getInternalState(history.get(0), ModuleResultXml.class));
	}

	private void assertDeepDtoEquals(ModuleResultXml xml) {
		ModuleResultXml spy = spy(xml);
		doReturn(entity.getParent()).when(spy).parent();
		assertDeepDtoEquals(entity, spy);
	}
}