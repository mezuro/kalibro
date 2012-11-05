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
}