package org.checkstyle;

import static org.junit.Assert.assertEquals;

import com.puppycrawl.tools.checkstyle.Checker;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Checker.class, CheckstyleConfiguration.class, CheckstyleMetricCollector.class, FileUtils.class})
public class CheckstyleMetricCollectorTest extends UnitTest {

	private File codeDirectory;
	private Set<NativeMetric> wantedMetrics;
	private Writer<NativeModuleResult> resultWriter;

	private CheckstyleMetricCollector collector;

	@Before
	public void setUp() throws Exception {
		codeDirectory = mock(File.class);
		wantedMetrics = mock(Set.class);
		resultWriter = mock(Writer.class);
		collector = new CheckstyleMetricCollector();
	}

	@Test
	public void shouldHaveNameAndDescription() throws IOException {
		assertEquals("Checkstyle", collector.name());
		assertEquals(loadResource("description"), collector.description());
	}

	@Test
	public void shouldGetSupportedMetrics() {
		assertDeepEquals(CheckstyleMetric.supportedMetrics(), collector.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		CheckstyleListener listener = mockListener();
		CheckstyleConfiguration configuration = mockConfiguration();
		List<File> javaFiles = mockJavaFiles();
		Checker checker = mockChecker();

		collector.collectMetrics(codeDirectory, wantedMetrics, resultWriter);
		InOrder order = Mockito.inOrder(checker);
		order.verify(checker).setModuleClassLoader(Checker.class.getClassLoader());
		order.verify(checker).addListener(listener);
		order.verify(checker).configure(configuration);
		order.verify(checker).process(javaFiles);
		order.verify(checker).destroy();
	}

	private CheckstyleListener mockListener() throws Exception {
		CheckstyleListener listener = mock(CheckstyleListener.class);
		whenNew(CheckstyleListener.class).withArguments(codeDirectory, wantedMetrics, resultWriter)
			.thenReturn(listener);
		return listener;
	}

	private CheckstyleConfiguration mockConfiguration() {
		CheckstyleConfiguration configuration = mock(CheckstyleConfiguration.class);
		mockStatic(CheckstyleConfiguration.class);
		when(CheckstyleConfiguration.checkerConfiguration(wantedMetrics)).thenReturn(configuration);
		return configuration;
	}

	private List<File> mockJavaFiles() {
		List<File> javaFiles = mock(List.class);
		mockStatic(FileUtils.class);
		when(FileUtils.listFiles(codeDirectory, new String[]{"java"}, true)).thenReturn(javaFiles);
		return javaFiles;
	}

	private Checker mockChecker() throws Exception {
		Checker checker = mock(Checker.class);
		whenNew(Checker.class).withNoArguments().thenReturn(checker);
		return checker;
	}
}