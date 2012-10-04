package org.kalibro.service.xml;

import static org.kalibro.ProjectResultFixtures.helloWorldResult;

import org.kalibro.Processing;

public class ProjectResultXmlTest extends XmlTest<Processing> {

	@Override
	protected Processing loadFixture() {
		return helloWorldResult();
	}
}