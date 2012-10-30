package org.kalibro.service.xml;

import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.RepositoryType;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dto.DaoLazyLoader;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public class RepositoryXmlTest extends XmlTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Configuration configuration = Whitebox.getInternalState(entity, "configuration");
		mockStatic(DaoLazyLoader.class);
		when(DaoLazyLoader.createProxy(eq(ConfigurationDao.class), eq("get"), any())).thenReturn(configuration);
	}

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertElement("license", String.class);
		assertElement("processPeriod", Integer.class);
		assertElement("type", RepositoryType.class, true);
		assertElement("address", String.class, true);
		assertElement("configurationId", Long.class, true);
	}
}