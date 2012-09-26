package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.MetricFixtures.analizoMetric;
import static org.kalibro.ModuleFixtures.helloWorldClass;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class AbstractModuleResultTest extends UnitTest {

	private NativeMetric acc, dit, loc;
	private NativeMetricResult accResult, locResult;
	private MyModuleResult moduleResult;

	@Before
	public void setUp() {
		acc = analizoMetric("acc");
		dit = analizoMetric("dit");
		loc = analizoMetric("loc");
		accResult = new NativeMetricResult(acc, 4.2);
		locResult = new NativeMetricResult(loc, 42.0);
		moduleResult = new MyModuleResult(helloWorldClass());
		moduleResult.addMetricResult(accResult);
		moduleResult.addMetricResult(locResult);
	}

	@Test
	public void testHasResultFor() {
		assertTrue(moduleResult.hasResultFor(acc));
		assertTrue(moduleResult.hasResultFor(loc));
		assertFalse(moduleResult.hasResultFor(dit));
	}

	@Test
	public void testGetResultFor() {
		assertSame(accResult, moduleResult.getResultFor(acc));
		assertSame(locResult, moduleResult.getResultFor(loc));
	}

	@Test
	public void checkErrorForInexistentResultMetric() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				moduleResult.getResultFor(dit);
			}
		}).throwsException().withMessage("No result found for metric: Depth of Inheritance Tree");
	}

	@Test
	public void testAddMetricResult() {
		assertDeepEquals(asSet(accResult, locResult), metricResults());

		NativeMetricResult ditResult = new NativeMetricResult(dit, 0.42);
		moduleResult.addMetricResult(ditResult);
		assertDeepEquals(asSet(accResult, ditResult, locResult), metricResults());
	}

	private Set<NativeMetricResult> metricResults() {
		return new HashSet<NativeMetricResult>(moduleResult.getMetricResults());
	}

	@Test
	public void shouldSortByModule() {
		assertSorted(newResult(SOFTWARE, "G"), newResult(SOFTWARE, "H"),
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