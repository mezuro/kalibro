package org.kalibro.client;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.kalibro.ModuleResult;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.xml.ModuleResultXml;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ModuleResultClientDao.class)
public class ModuleResultClientDaoTest extends
	ClientTest<ModuleResult, ModuleResultXml, ModuleResultXml, ModuleResultEndpoint, ModuleResultClientDao> {

	private static final String PROJECT_NAME = "ModuleResultClientDaoTest project name";
	private static final String MODULE_NAME = "ModuleResultClientDaoTest module name";
	private static final Date DATE = new Date();

	@Override
	protected Class<ModuleResult> entityClass() {
		return ModuleResult.class;
	}

	@Test
	public void testGetModuleResult() {
		when(port.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE)).thenReturn(response);
		assertSame(entity, client.getModuleResult(PROJECT_NAME, MODULE_NAME, DATE));
	}

	@Test
	public void testResultHistory() {
		when(port.getResultHistory(PROJECT_NAME, MODULE_NAME)).thenReturn(Arrays.asList(response));
		assertDeepList(client.getResultHistory(PROJECT_NAME, MODULE_NAME), entity);
	}
}