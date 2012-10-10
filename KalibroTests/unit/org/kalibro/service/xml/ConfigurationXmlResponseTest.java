package org.kalibro.service.xml;

import org.junit.Test;
import org.kalibro.Configuration;

public class ConfigurationXmlResponseTest extends XmlTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return loadFixture("sc", Configuration.class);
	}

	@Test
	public void verifyElements() {
		assertElement("id", Long.class, false);
		assertElement("name", String.class, false);
		assertElement("description", String.class, false);
	}
}