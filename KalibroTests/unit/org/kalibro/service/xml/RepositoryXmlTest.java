package org.kalibro.service.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;

public class RepositoryXmlTest extends XmlTest {

	@Override
	protected void verifyElements() {
		assertElement("id", Long.class);
		assertElement("name", String.class, true);
		assertElement("description", String.class);
		assertElement("license", String.class);
		assertElement("processPeriod", Integer.class);
		assertElement("processHistorically", boolean.class);
		assertElement("type", RepositoryType.class, true);
		assertElement("address", String.class, true);
		assertElement("configurationId", Long.class, true);
	}

	@Test
	public void shouldRetrieveConfigurationId() {
		Repository repository = (Repository) entity;
		RepositoryXml xml = (RepositoryXml) dto;
		assertEquals(repository.getConfiguration().getId(), xml.configurationId());
	}
}