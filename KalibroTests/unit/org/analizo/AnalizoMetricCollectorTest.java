package org.analizo;

import static org.analizo.AnalizoStub.*;

import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandExecutor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalizoMetricCollector.class)
public class AnalizoMetricCollectorTest extends KalibroTestCase {

	private AnalizoStub analizoStub;
	private CommandExecutor executor;

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws Exception {
		analizoStub = new AnalizoStub();
		executor = PowerMockito.mock(CommandExecutor.class);
		mockOutput("analizo metrics --list", "Analizo-Output-MetricList.txt");
		analizo = new AnalizoMetricCollector();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSupportedMetrics() {
		assertDeepEquals(analizoStub.getSupportedMetrics(), analizo.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File("/");
		mockOutput("analizo metrics /", "Analizo-Output-HelloWorld.txt");
		assertDeepEquals(collectMetrics(), analizo.collectMetrics(codeDirectory, nativeMetrics()));
	}

	private void mockOutput(String command, String outputResource) throws Exception {
		InputStream output = getClass().getResourceAsStream(outputResource);
		PowerMockito.whenNew(CommandExecutor.class).withArguments(command).thenReturn(executor);
		PowerMockito.when(executor.executeCommandAndGetOuput()).thenReturn(output);
	}
}