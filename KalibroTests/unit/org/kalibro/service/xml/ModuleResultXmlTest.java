package org.kalibro.service.xml;

import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;

import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.ModuleResult;

public class ModuleResultXmlTest extends XmlTest<ModuleResult> {

	@Override
	protected ModuleResult loadFixture() {
		ModuleResult fixture = newHelloWorldClassResult();
		fixture.addCompoundMetricWithError(new CompoundMetric(), new Exception());
		return fixture;
	}
}