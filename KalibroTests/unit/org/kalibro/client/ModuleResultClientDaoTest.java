package org.kalibro.client;

import static org.junit.Assert.assertSame;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ModuleResultClientDao.class)
public class ModuleResultClientDaoTest extends
	ClientTest<ModuleResult, ModuleResultXml, ModuleResultEndpoint, ModuleResultClientDao> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Class<ModuleResult> entityClass() {
		return ModuleResult.class;
	}

	@Test
	public void shouldGetResultsRoot() {
		when(port.resultsRootOf(ID)).thenReturn(xml);
		assertSame(entity, client.resultsRootOf(ID));
	}

	@Test
	public void shouldGetParent() {
		when(port.parentOf(ID)).thenReturn(xml);
		assertSame(entity, client.parentOf(ID));
	}

	@Test
	public void shouldGetChildren() {
		when(port.childrenOf(ID)).thenReturn(list(xml));
		assertDeepEquals(set(entity), client.childrenOf(ID));
	}
}