package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.core.persistence.record.MetricConfigurationSnapshotRecord;
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
	public void shouldGetConfigurationOfRepository() {
		String from = "Repository repository JOIN repository.configuration configuration";
		TypedQuery<ConfigurationRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createRecordQuery(from, "repository.id = :repositoryId");
		when(query.getSingleResult()).thenReturn(record);

		assertSame(configuration, dao.configurationOf(ID));
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldSave() {
		doReturn(record).when(dao).save(record);
		assertEquals(ID, dao.save(configuration));
		verify(dao).save(record);
	}

	@Test
	public void shouldGetSnapshotForProcessing() {
		MetricConfiguration snapshot = mock(MetricConfiguration.class);
		MetricConfigurationSnapshotRecord snapshotRecord = mock(MetricConfigurationSnapshotRecord.class);
		TypedQuery<MetricConfigurationSnapshotRecord> query = mock(TypedQuery.class);
		doReturn(query).when(dao).createQuery(
			"SELECT snapshot FROM MetricConfigurationSnapshot snapshot WHERE snapshot.processing.id = :processingId",
			MetricConfigurationSnapshotRecord.class);
		when(query.getResultList()).thenReturn(list(snapshotRecord));
		when(snapshotRecord.convert()).thenReturn(snapshot);

		assertDeepEquals(set(snapshot), dao.snapshotFor(ID).getMetricConfigurations());
		verify(query).setParameter("processingId", ID);
	}
}