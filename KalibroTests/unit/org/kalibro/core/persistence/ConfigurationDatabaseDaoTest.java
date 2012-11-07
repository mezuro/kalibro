package org.kalibro.core.persistence;

import static org.junit.Assert.*;

import java.util.Random;

import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Configuration;
import org.kalibro.MetricConfiguration;
import org.kalibro.core.persistence.record.ConfigurationRecord;
import org.kalibro.core.persistence.record.MetricConfigurationSnapshotRecord;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigurationDatabaseDao.class)
public class ConfigurationDatabaseDaoTest extends
	DatabaseDaoTestCase<Configuration, ConfigurationRecord, ConfigurationDatabaseDao> {

	private static final Long ID = new Random().nextLong();

	@Test
	public void shouldGetConfigurationOfRepository() {
		assertSame(entity, dao.configurationOf(ID));

		String from = "Repository repository JOIN repository.configuration configuration";
		verify(dao).createRecordQuery(from, "repository.id = :repositoryId");
		verify(query).setParameter("repositoryId", ID);
	}

	@Test
	public void shouldSave() throws Exception {
		when(record.id()).thenReturn(ID);
		assertEquals(ID, dao.save(entity));

		verifyNew(ConfigurationRecord.class).withArguments(entity);
		verify(dao).save(record);
	}

	@Test
	public void shouldGetSnapshotForProcessing() {
		MetricConfiguration snapshot = mock(MetricConfiguration.class);
		MetricConfigurationSnapshotRecord snapshotRecord = mock(MetricConfigurationSnapshotRecord.class);
		TypedQuery<MetricConfigurationSnapshotRecord> snapshotQuery = mock(TypedQuery.class);
		doReturn(snapshotQuery).when(dao).createQuery(
			"SELECT snapshot FROM MetricConfigurationSnapshot snapshot WHERE snapshot.processing.id = :processingId",
			MetricConfigurationSnapshotRecord.class);
		when(snapshotQuery.getResultList()).thenReturn(list(snapshotRecord));
		when(snapshotRecord.convert()).thenReturn(snapshot);

		assertDeepEquals(set(snapshot), dao.snapshotFor(ID).getMetricConfigurations());
		verify(snapshotQuery).setParameter("processingId", ID);
	}
}