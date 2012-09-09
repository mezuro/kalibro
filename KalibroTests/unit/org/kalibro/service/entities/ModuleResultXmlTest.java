package org.kalibro.service.entities;

import static org.junit.Assert.assertTrue;
import static org.kalibro.core.model.ModuleResultFixtures.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.DtoTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.CompoundMetric;
import org.kalibro.core.model.ModuleResult;
import org.powermock.reflect.internal.WhiteboxImpl;

public class ModuleResultXmlTest extends DtoTestCase<ModuleResult, ModuleResultXml> {

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultConstructorOfCompoundMetricWithErrorShouldDoNothing() {
		checkException(new Task() {

			@Override
			public void perform() {
				new CompoundMetricWithErrorXml().getError();
			}
		}, NullPointerException.class);
	}

	@Override
	protected ModuleResultXml newDtoUsingDefaultConstructor() {
		return new ModuleResultXml();
	}

	@Override
	protected Collection<ModuleResult> entitiesForTestingConversion() {
		ModuleResult resultWithError = newHelloWorldClassResult();
		resultWithError.addCompoundMetricWithError(new CompoundMetric(), new Exception());
		return Arrays.asList(helloWorldApplicationResult(), helloWorldClassResult(), resultWithError);
	}

	@Override
	protected ModuleResultXml createDto(ModuleResult moduleResult) {
		return new ModuleResultXml(moduleResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConvertNullCompoundMetricsWithErrorIntoEmptyMap() {
		ModuleResult moduleResult = helloWorldClassResult();
		ModuleResultXml dto = createDto(moduleResult);
		WhiteboxImpl.setInternalState(dto, "compoundMetricsWithError", (Object) null);
		assertTrue(dto.convert().getCompoundMetricsWithError().isEmpty());
	}
}