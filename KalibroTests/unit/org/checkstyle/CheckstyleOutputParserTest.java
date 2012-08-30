package org.checkstyle;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.NativeModuleResult;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditEvent.class, LocalizedMessage.class})
public class CheckstyleOutputParserTest extends TestCase {

	private static final CheckstyleMetric METRIC = CheckstyleMetric.FAN_OUT;
	private static final NativeMetric NATIVE_METRIC = METRIC.getNativeMetric();
	private static final Double VALUE = 42.0;

	private CheckstyleOutputParser parser;

	@Before
	public void setUp() {
		Set<NativeMetric> wantedMetrics = new HashSet<NativeMetric>(Arrays.asList(NATIVE_METRIC));
		parser = new CheckstyleOutputParser(repositoriesDirectory(), wantedMetrics);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void resultsShouldBeEmptyBeforeCheckstyleExecution() {
		assertTrue(parser.getResults().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricResults() {
		simulateCheckstyle("" + VALUE);
		verifyResult(VALUE);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void metricResultShouldBeZeroIfMessageIsNotANumber() {
		simulateCheckstyle("");
		verifyResult(0.0);
	}

	private void simulateCheckstyle(String message) {
		parser.auditStarted(null);
		parser.addError(mockEvent(message));
		parser.auditFinished(null);
	}

	private AuditEvent mockEvent(String message) {
		LocalizedMessage localizedMessage = PowerMockito.mock(LocalizedMessage.class);
		PowerMockito.when(localizedMessage.getKey()).thenReturn(METRIC.getMessageKey());

		AuditEvent event = PowerMockito.mock(AuditEvent.class);
		PowerMockito.when(event.getLocalizedMessage()).thenReturn(localizedMessage);
		PowerMockito.when(event.getFileName()).thenReturn(repositoriesDirectory() + "/org/fibonacci/Fibonacci.java");
		PowerMockito.when(event.getMessage()).thenReturn(message);
		return event;
	}

	private void verifyResult(Double value) {
		NativeModuleResult moduleResult = verifyUniqueResult();
		assertDeepEquals(CheckstyleStub.result().getModule(), moduleResult.getModule());

		NativeMetricResult metricResult = verifyUniqueMetricResult(moduleResult);
		assertDeepEquals(NATIVE_METRIC, metricResult.getMetric());
		assertDoubleEquals(value, metricResult.getValue());
	}

	private NativeModuleResult verifyUniqueResult() {
		Set<NativeModuleResult> results = parser.getResults();
		assertEquals(1, results.size());
		return results.iterator().next();
	}

	private NativeMetricResult verifyUniqueMetricResult(NativeModuleResult moduleResult) {
		Collection<NativeMetricResult> metricResults = moduleResult.getMetricResults();
		assertEquals(1, metricResults.size());
		return metricResults.iterator().next();
	}
}