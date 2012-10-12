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
	public void shouldGetDaos() throws Exception {
		shouldGetDao(BaseToolClientDao.class);
		shouldGetDao(ConfigurationClientDao.class);
		shouldGetDao(MetricConfigurationClientDao.class);
		shouldGetDao(MetricResultClientDao.class);
		shouldGetDao(ModuleResultClientDao.class);
		shouldGetDao(ProcessingClientDao.class);
		shouldGetDao(ProjectClientDao.class);
		shouldGetDao(RangeClientDao.class);
		shouldGetDao(ReadingClientDao.class);
		shouldGetDao(ReadingGroupClientDao.class);
		shouldGetDao(RepositoryClientDao.class);
	}

	private <T> void shouldGetDao(Class<T> daoClass) throws Exception {
		T dao = mock(daoClass);
		whenNew(daoClass).withArguments(SERVICE_ADDRESS).thenReturn(dao);
		assertSame(dao, Whitebox.invokeMethod(factory, "create" + daoClass.getSimpleName().replace("Client", "")));
	}
}