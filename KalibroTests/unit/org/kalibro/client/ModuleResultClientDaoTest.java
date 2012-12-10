package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.xml.DateModuleResultXml;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest({DateModuleResultXml.class, ModuleResultClientDao.class})
public class ModuleResultClientDaoTest extends
	ClientTest<ModuleResult, ModuleResultXml, ModuleResultEndpoint, ModuleResultClientDao> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Class<ModuleResult> entityClass() {
		return ModuleResult.class;
	}

	@Test
	public void shouldGetById() {
		when(port.getModuleResult(ID)).thenReturn(xml);
		assertSame(entity, client.get(ID));
	}

	@Test
	public void shouldGetChildren() {
		when(port.childrenOf(ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.childrenOf(ID));
	}

	@Test
	public void shouldGetHistory() {
		Date date = new Date(1);
		List<DateModuleResultXml> history = new ArrayList<DateModuleResultXml>();
		history.add(new DateModuleResultXml(date, entity));
		when(xml.convert()).thenReturn(entity);
		when(port.historyOfModule(ID)).thenReturn(history);

		SortedMap<Date, ModuleResult> map = client.historyOf(ID);
		assertEquals(1, map.size());
		assertDeepEquals(set(date), map.keySet());
		assertDeepEquals(entity, map.get(date));
	}
}