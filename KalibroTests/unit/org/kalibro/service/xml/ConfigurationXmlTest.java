package org.kalibro.service.xml;

import static org.kalibro.ConfigurationFixtures.newConfiguration;

import org.kalibro.Configuration;

public class ConfigurationXmlTest extends XmlTest<Configuration> {

	@Override
	protected Configuration loadFixture() {
		return newConfiguration("loc");
	}
}