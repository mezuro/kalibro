package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationDatabaseDao.class)
public class ConfigurationDatabaseDaoTest extends UnitTest {

	private static final Long ID = new Random().nextLong();

	private Configuration configuration;
	private ConfigurationRecord record;

	private ConfigurationDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		configuration = mock(Configuration.class);
		record = mock(ConfigurationRecord.class);
		whenNew(ConfigurationRecord.class).withArguments(configuration).thenReturn(record);
		when(record.convert()).thenReturn(configuration);
		when(record.id()).thenReturn(ID);
		dao = spy(new ConfigurationDatabaseDao(null));
	}

	@Test
	public void shouldGetConfigurationOfProject() {
		TypedQuery<ConfigurationRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery("JOIN Project project WHERE project.id = :projectId");
		when(query.getSingleResult()).thenReturn(record);

		assertSame(configuration, dao.configurationOf(ID));
		verify(query).setParameter("projectId", ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(configuration));
		verify(dao).save(record);
	}
}