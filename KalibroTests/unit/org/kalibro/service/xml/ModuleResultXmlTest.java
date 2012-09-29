package org.kalibro.service.xml;

import static org.kalibro.ModuleResultFixtures.newHelloWorldClassResult;

import org.kalibro.CompoundMetric;
import org.kalibro.ModuleResult;

public class ModuleResultXmlTest extends XmlTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		ModuleResult fixture = newHelloWorldClassResult();
		fixture.addCompoundMetricWithError(new CompoundMetric(), new Exception());
		return fixture;
	}
}