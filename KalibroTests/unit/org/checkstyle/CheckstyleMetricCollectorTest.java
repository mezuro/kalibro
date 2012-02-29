package org.checkstyle;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckstyleConfiguration.class, CheckstyleMetricCollector.class})
public class CheckstyleMetricCollectorTest extends KalibroTestCase {

	private CheckstyleMetricCollector collector;

	private KalibroChecker checker;
	private CheckstyleOutputParser parser;
	private CheckstyleConfiguration configuration;
	private Set<NativeMetric> wantedMetrics;

	@Before
	public void setUp() throws Exception {
		collector = new CheckstyleMetricCollector();
		wantedMetrics = collector.getSupportedMetrics();
		mockParser();
		mockConfiguration();
		mockChecker();
	}

	private void mockParser() throws Exception {
		parser = PowerMockito.mock(CheckstyleOutputParser.class);
		PowerMockito.whenNew(CheckstyleOutputParser.class).withArguments(wantedMetrics).thenReturn(parser);
	}

	private void mockConfiguration() {
		configuration = PowerMockito.mock(CheckstyleConfiguration.class);
		PowerMockito.mockStatic(CheckstyleConfiguration.class);
		PowerMockito.when(CheckstyleConfiguration.checkerConfiguration(wantedMetrics)).thenReturn(configuration);
	}

	private void mockChecker() throws Exception {
		checker = PowerMockito.mock(KalibroChecker.class);
		PowerMockito.whenNew(KalibroChecker.class).withArguments(parser, configuration).thenReturn(checker);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSupportedMetrics() {
		assertDeepEquals(CheckstyleStub.nativeMetrics(), collector.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = PowerMockito.mock(File.class);
		Set<NativeModuleResult> results = CheckstyleStub.results();
		PowerMockito.when(parser.getResults()).thenReturn(results);

		assertSame(results, collector.collectMetrics(codeDirectory, wantedMetrics));
		Mockito.verify(checker).process(codeDirectory);
		Mockito.verify(parser).getResults();
	}
}