package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.ModuleResultFixtures.newHelloWorldResultMap;
import static org.kalibro.ProjectFixtures.helloWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.dao.ConfigurationDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, KalibroSettings.class})
public class CollectMetricsTaskTest extends UnitTest {

	private BaseTool baseTool;
	private Processing processing;

	private CollectMetricsTask collectTask;

	@Before
	public void setUp() {
		baseTool = new BaseTool(MetricCollectorStub.CLASS_NAME);
		processing = new Processing(helloWorld());
		mockKalibro();
		collectTask = new CollectMetricsTask(processing);
	}

	private void mockKalibro() {
		KalibroSettings settings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
		when(settings.getServerSettings()).thenReturn(mock(ServerSettings.class));
		mockConfiguration();
	}

	private void mockConfiguration() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		Configuration configuration = mock(Configuration.class);
		Map<BaseTool, Set<NativeMetric>> metricsMap = new HashMap<BaseTool, Set<NativeMetric>>();
		metricsMap.put(baseTool, baseTool.getSupportedMetrics());

		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(configurationDao);
		when(configurationDao.configurationOf(processing.getProject().getId())).thenReturn(configuration);
		when(configuration.getNativeMetrics()).thenReturn(metricsMap);
	}

	@Test
	public void checkTaskState() {
		assertEquals(ProcessState.COLLECTING, collectTask.getTaskState());
	}

	@Test
	public void shouldReturnCollectedResults() throws Exception {
		assertDeepEquals(newHelloWorldResultMap(processing.getDate()), collectTask.compute());
	}
}