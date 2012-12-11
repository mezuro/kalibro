package org.cvsanaly;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class, CvsAnalyMetricCollector.class})
public class CvsAnalyMetricCollectorTest extends UnitTest {

	private static final String COMMAND = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d /";

	private File databaseFile;
	private CommandTask commandTask;
	private Set<NativeMetric> wantedMetrics;
	private CvsAnalyDatabaseFetcher fetcher;

	private CvsAnalyMetricCollector collector;

	@Before
	public void setUp() throws Exception {
		createMocks();
		whenNew(CommandTask.class).withArguments("cvsanaly2 --version").thenReturn(commandTask);
		collector = new CvsAnalyMetricCollector();
	}

	private void createMocks() throws Exception {
		databaseFile = mock(File.class);
		commandTask = mock(CommandTask.class);
		wantedMetrics = mock(Set.class);
		fetcher = mock(CvsAnalyDatabaseFetcher.class);
		mockStatic(File.class);
		when(File.createTempFile("kalibro-cvsanaly-db", ".sqlite")).thenReturn(databaseFile);
		when(databaseFile.getAbsolutePath()).thenReturn("/");
		whenNew(CvsAnalyDatabaseFetcher.class).withArguments(wantedMetrics).thenReturn(fetcher);
	}

	@Test
	public void shouldHaveNameAndDescription() throws IOException {
		assertEquals("CVSAnalY", collector.name());
		assertEquals(loadResource("description"), collector.description());
	}

	@Test
	public void shouldGetSupportedMetrics() {
		assertDeepEquals(CvsAnalyMetric.supportedMetrics(), collector.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = mock(File.class);
		Writer<NativeModuleResult> resultWriter = mock(Writer.class);

		Mockito.reset(commandTask);
		whenNew(CommandTask.class).withArguments(COMMAND, codeDirectory).thenReturn(commandTask);

		collector.collectMetrics(codeDirectory, wantedMetrics, resultWriter);
		InOrder order = Mockito.inOrder(commandTask, fetcher);
		order.verify(commandTask).execute();
		order.verify(fetcher).queryMetrics(databaseFile, resultWriter);
	}
}