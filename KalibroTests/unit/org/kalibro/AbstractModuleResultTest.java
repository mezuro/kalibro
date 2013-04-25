package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class AbstractModuleResultTest extends UnitTest {

	private Module module;
	private NativeMetric cbo, lcom4;
	private NativeMetricResult cboResult, lcom4Result;

	private AbstractModuleResult<NativeMetricResult> moduleResult;

	@Before
	public void setUp() {
		module = mock(Module.class);
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		cboResult = new NativeMetricResult(cbo, 28.0);
		lcom4Result = new NativeMetricResult(lcom4, 42.0);
		moduleResult = moduleResult(module);
	}

	@Test
	public void shouldSortByModule() {
		Module second = mock(Module.class);
		when(module.compareTo(second)).thenReturn(-1);
		when(second.compareTo(module)).thenReturn(1);
		assertSorted(moduleResult, moduleResult(second));
	}

	@Test
	public void shouldIdentifyByModule() {
		AbstractModuleResult<NativeMetricResult> other = moduleResult(module);
		other.addMetricResult(cboResult);
		other.addMetricResult(lcom4Result);
		assertEquals(moduleResult, other);
	}

	private AbstractModuleResult<NativeMetricResult> moduleResult(Module theModule) {
		return new AbstractModuleResult<NativeMetricResult>(theModule)
			{ /* just for test */ };
	}

	@Test
	public void checkConstruction() {
		assertSame(module, moduleResult.getModule());
		assertTrue(moduleResult.getMetricResults().isEmpty());
	}

	@Test
	public void shouldAnswerIfHasResultForMetric() {
		moduleResult.addMetricResult(cboResult);
		assertTrue(moduleResult.hasResultFor(cbo));
		assertFalse(moduleResult.hasResultFor(lcom4));
	}

	@Test
	public void shouldGetResultForMetric() {
		moduleResult.addMetricResult(cboResult);
		assertSame(cboResult, moduleResult.getResultFor(cbo));
		assertThat(getResultFor(lcom4)).throwsException().withMessage("No result found for metric: " + lcom4);
	}

	private VoidTask getResultFor(final Metric metric) {
		return new VoidTask() {

			@Override
			protected void perform() {
				moduleResult.getResultFor(metric);
			}
		};
	}

	@Test
	public void shouldAddMetricResult() {
		moduleResult.addMetricResult(cboResult);
		moduleResult.addMetricResult(lcom4Result);
		assertDeepEquals(set(cboResult, lcom4Result), moduleResult.getMetricResults());
	}

	@Test
	public void shouldReplaceMetricOnAddingEquivalent() {
		moduleResult.addMetricResult(cboResult);
		assertDoubleEquals(28.0, moduleResult.getResultFor(cbo).getValue());

		moduleResult.addMetricResult(new NativeMetricResult(cbo, 496.0));
		assertDoubleEquals(496.0, moduleResult.getResultFor(cbo).getValue());
	}
}