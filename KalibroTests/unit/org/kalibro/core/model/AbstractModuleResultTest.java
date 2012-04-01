package org.kalibro.core.model;

import static org.analizo.AnalizoStub.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Granularity;

public class AbstractModuleResultTest extends KalibroTestCase {

	private NativeMetric acc, dit, loc;
	private NativeMetricResult accResult, locResult;
	private MyModuleResult moduleResult;

	@Before
	public void setUp() {
		acc = nativeMetric("acc");
		dit = nativeMetric("dit");
		loc = nativeMetric("loc");
		accResult = new NativeMetricResult(acc, 4.2);
		locResult = new NativeMetricResult(loc, 42.0);
		moduleResult = new MyModuleResult(helloWorldClass());
		moduleResult.addMetricResult(accResult);
		moduleResult.addMetricResult(locResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultFor() {
		assertTrue(moduleResult.hasResultFor(acc));
		assertTrue(moduleResult.hasResultFor(loc));
		assertFalse(moduleResult.hasResultFor(dit));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetResultFor() {
		assertSame(accResult, moduleResult.getResultFor(acc));
		assertSame(locResult, moduleResult.getResultFor(loc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorForInexistentResultMetric() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				moduleResult.getResultFor(dit);
			}
		}, "No result found for metric: Depth of Inheritance Tree");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddMetricResult() {
		assertDeepEquals(moduleResult.getMetricResults(), accResult, locResult);

		NativeMetricResult ditResult = new NativeMetricResult(dit, 0.42);
		moduleResult.addMetricResult(ditResult);
		assertDeepEquals(moduleResult.getMetricResults(), accResult, ditResult, locResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByModule() {
		assertSorted(newResult(APPLICATION, "G"), newResult(APPLICATION, "H"),
			newResult(PACKAGE, "E"), newResult(PACKAGE, "F"),
			newResult(CLASS, "C"), newResult(CLASS, "D"),
			newResult(METHOD, "A"), newResult(METHOD, "B"));
	}

	private MyModuleResult newResult(Granularity granularity, String name) {
		return new MyModuleResult(new Module(granularity, name));
	}

	private class MyModuleResult extends AbstractModuleResult<NativeMetricResult> {

		public MyModuleResult(Module module) {
			super(module);
		}
	}
}