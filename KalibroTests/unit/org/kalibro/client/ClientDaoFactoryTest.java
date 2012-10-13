package org.kalibro.client;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientDaoFactory.class)
public class ClientDaoFactoryTest extends UnitTest {

	private static final String SERVICE_ADDRESS = "ClientDaoFactoryTest service address";

	private ClientDaoFactory factory;

	@Before
	public void setUp() {
		factory = new ClientDaoFactory(SERVICE_ADDRESS);
	}

	@Test
	public void shouldCreateDaos() throws Exception {
		shouldCreate(BaseToolClientDao.class);
		shouldCreate(ConfigurationClientDao.class);
		shouldCreate(MetricConfigurationClientDao.class);
		shouldCreate(MetricResultClientDao.class);
		shouldCreate(ModuleResultClientDao.class);
		shouldCreate(ProcessingClientDao.class);
		shouldCreate(ProjectClientDao.class);
		shouldCreate(RangeClientDao.class);
		shouldCreate(ReadingClientDao.class);
		shouldCreate(ReadingGroupClientDao.class);
		shouldCreate(RepositoryClientDao.class);
	}

	private <T> void shouldCreate(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		whenNew(daoClass).withArguments(SERVICE_ADDRESS).thenReturn(dao);
		assertSame(dao, Whitebox.invokeMethod(factory, "create" + daoClass.getSimpleName().replace("Client", "")));
	}
}