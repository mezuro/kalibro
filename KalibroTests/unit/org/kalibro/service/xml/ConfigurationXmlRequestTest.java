package org.kalibro.service.xml;

import static org.kalibro.ConfigurationFixtures.newConfiguration;

import org.kalibro.Configuration;

public class ConfigurationXmlRequestTest extends XmlTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return newConfiguration("loc");
	}
}