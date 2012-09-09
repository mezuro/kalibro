package org.checkstyle;

import static org.junit.Assert.assertSame;

import com.puppycrawl.tools.checkstyle.Checker;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Checker.class, CheckstyleConfiguration.class, CheckstyleMetricCollector.class, FileUtils.class})
public class CheckstyleMetricCollectorTest extends TestCase {

	private static final Set<NativeMetric> METRICS = CheckstyleStub.nativeMetrics();

	private CheckstyleConfiguration configuration;
	private CheckstyleOutputParser parser;
	private List<File> files;
	private Checker checker;

	private CheckstyleMetricCollector collector;

	@Before
	public void setUp() throws Exception {
		collector = new CheckstyleMetricCollector();
		mockConfiguration();
		mockParser();
		mockFiles();
		mockChecker();
	}

	private void mockConfiguration() {
		configuration = mock(CheckstyleConfiguration.class);
		mockStatic(CheckstyleConfiguration.class);
		when(CheckstyleConfiguration.checkerConfiguration(METRICS)).thenReturn(configuration);
	}

	private void mockParser() throws Exception {
		parser = mock(CheckstyleOutputParser.class);
		whenNew(CheckstyleOutputParser.class).withArguments(repositoriesDirectory(), METRICS).thenReturn(parser);
	}

	private void mockFiles() {
		files = mock(List.class);
		mockStatic(FileUtils.class);
		when(FileUtils.listFiles(repositoriesDirectory(), new String[]{"java"}, true)).thenReturn(files);
	}

	private void mockChecker() throws Exception {
		checker = mock(Checker.class);
		whenNew(Checker.class).withNoArguments().thenReturn(checker);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSupportedMetrics() {
		assertDeepEquals(METRICS, collector.getBaseTool().getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		Set<NativeModuleResult> results = CheckstyleStub.results();
		when(parser.getResults()).thenReturn(results);

		assertSame(results, collector.collectMetrics(repositoriesDirectory(), METRICS));
		InOrder order = Mockito.inOrder(checker, parser);
		order.verify(checker).setModuleClassLoader(Checker.class.getClassLoader());
		order.verify(checker).addListener(parser);
		order.verify(checker).configure(configuration);
		order.verify(checker).process(files);
		order.verify(checker).destroy();
		order.verify(parser).getResults();
	}
}