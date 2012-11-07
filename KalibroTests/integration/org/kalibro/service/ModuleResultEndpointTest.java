package org.kalibro.service;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Granularity;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ModuleResultDao;
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
		return list("metricResults", "children");
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
}