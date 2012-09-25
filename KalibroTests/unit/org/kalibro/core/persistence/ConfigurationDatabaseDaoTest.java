package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.List;
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

	private static final Long ID = Math.abs(new Random().nextLong());

	private Configuration configuration;
	private ConfigurationRecord record;

	private ConfigurationDatabaseDao dao;

	@Before
	public void setUp() throws Exception {
		configuration = mock(Configuration.class);
		record = mock(ConfigurationRecord.class);
		when(record.convert()).thenReturn(configuration);
		whenNew(ConfigurationRecord.class).withArguments(configuration).thenReturn(record);
		dao = spy(new ConfigurationDatabaseDao(null));
	}

	@Test
	public void shouldConfirmExistence() {
		doReturn(true).when(dao).existsWithId(ID);
		assertTrue(dao.exists(ID));

		doReturn(false).when(dao).existsWithId(ID);
		assertFalse(dao.exists(ID));
	}

	@Test
	public void shouldGetById() {
		doReturn(configuration).when(dao).getById(ID);
		assertSame(configuration, dao.get(ID));
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
	public void shouldGetAll() {
		List<Configuration> all = mock(List.class);
		doReturn(all).when(dao).allOrderedByName();
		assertSame(all, dao.all());
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		when(record.id()).thenReturn(ID);

		assertEquals(ID, dao.save(configuration));
		verify(dao).save(record);
	}

	@Test
	public void shouldDelete() {
		doNothing().when(dao).deleteById(ID);
		dao.delete(ID);
		verify(dao).deleteById(ID);
	}
}