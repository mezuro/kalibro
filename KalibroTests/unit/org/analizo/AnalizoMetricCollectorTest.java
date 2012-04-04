package org.analizo;

import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.kalibro.core.model.NativeModuleResultFixtures.*;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.NativeMetric;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalizoMetricCollector.class)
public class AnalizoMetricCollectorTest extends KalibroTestCase {

	private CommandTask executor;

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws Exception {
		executor = PowerMockito.mock(CommandTask.class);
		mockOutput("analizo metrics --list", "Analizo-Output-MetricList.txt");
		analizo = new AnalizoMetricCollector();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkBaseTool() {
		assertDeepEquals(analizo(), analizo.getBaseTool());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		mockOutput("analizo metrics /", "Analizo-Output-HelloWorld.txt");
		assertDeepEquals(analizo.collectMetrics(codeDirectory, metrics),
			helloWorldApplicationResult(), helloWorldClassResult());
	}

	private void mockOutput(String command, String outputResource) throws Exception {
		InputStream output = getClass().getResourceAsStream(outputResource);
		PowerMockito.whenNew(CommandTask.class).withArguments(command).thenReturn(executor);
		PowerMockito.when(executor.executeAndGetOuput()).thenReturn(output);
	}
}