package org.checkstyle;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetric;
import org.kalibro.NativeMetricResult;
import org.kalibro.NativeModuleResult;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditEvent.class, LocalizedMessage.class})
public class CheckstyleOutputParserTest extends UnitTest {

	private static final String PATH = "/code/directory/absolute/path";
	private static final CheckstyleMetric METRIC = CheckstyleMetric.metricFor("maxLen.file");
	private static final Double VALUE = 42.0;

	private File codeDirectory;
	private CheckstyleOutputParser parser;

	@Before
	public void setUp() {
		codeDirectory = mock(File.class);
		when(codeDirectory.getAbsolutePath()).thenReturn(PATH);
		Set<NativeMetric> wantedMetrics = set((NativeMetric) METRIC);
		parser = new CheckstyleOutputParser(codeDirectory, wantedMetrics);
	}

	@Test
	public void resultsShouldBeEmptyBeforeCheckstyleExecution() {
		assertTrue(parser.getResults().isEmpty());
	}

	@Test
	public void shouldParseMetricResults() {
		simulateCheckstyle("" + VALUE);
		verifyResult(VALUE);
	}

	@Test
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
		PowerMockito.when(event.getFileName()).thenReturn(PATH + "/org/fibonacci/Fibonacci.java");
		PowerMockito.when(event.getMessage()).thenReturn(message);
		return event;
	}

	private void verifyResult(Double value) {
		NativeModuleResult moduleResult = verifyUniqueResult();
		assertDeepEquals(CheckstyleStub.result().getModule(), moduleResult.getModule());

		NativeMetricResult metricResult = verifyUniqueMetricResult(moduleResult);
		assertDeepEquals(METRIC, metricResult.getMetric());
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