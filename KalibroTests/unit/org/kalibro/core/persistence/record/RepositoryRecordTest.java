package org.kalibro.core.persistence.record;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Repository;

public class RepositoryRecordTest extends RecordTest {

	@Override
	protected void verifyColumns() {
		shouldHaveId();
		assertColumn("project", Long.class).isRequired();
		assertColumn("name", String.class).isRequired();
		assertColumn("type", String.class).isRequired();
		assertColumn("address", String.class).isRequired();
		assertColumn("description", String.class).isNullable();
		assertColumn("license", String.class).isNullable();
		assertColumn("processPeriod", Integer.class).isNullable();
		assertColumn("configuration", Long.class).isRequired();
	}

	@Test
	public void shouldRetrieveConfigurationId() {
		Repository repository = (Repository) entity;
		RepositoryRecord record = (RepositoryRecord) dto;
		assertEquals(repository.getConfiguration().getId(), record.configurationId());
	}
}