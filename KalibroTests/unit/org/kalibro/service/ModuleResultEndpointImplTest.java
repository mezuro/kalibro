package org.kalibro.service;

import static org.junit.Assert.assertSame;

import java.util.Random;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.dao.ModuleResultDao;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(ModuleResultEndpointImpl.class)
public class ModuleResultEndpointImplTest extends
	EndpointImplementorTest<ModuleResult, ModuleResultXml, ModuleResultXml, ModuleResultDao, ModuleResultEndpointImpl> {

	private static final Long ID = new Random().nextLong();

	@Override
	protected Class<ModuleResult> entityClass() {
		return ModuleResult.class;
	}

	@Test
	public void shouldGetResultsRoot() {
		when(dao.resultsRootOf(ID)).thenReturn(entity);
		assertSame(response, implementor.resultsRootOf(ID));
	}

	@Test
	public void shouldGetParent() {
		when(dao.parentOf(ID)).thenReturn(entity);
		assertSame(response, implementor.parentOf(ID));
	}

	@Test
	public void shouldGetChildren() {
		when(dao.childrenOf(ID)).thenReturn(asSortedSet(entity));
		assertDeepEquals(asList(response), implementor.childrenOf(ID));
	}
}