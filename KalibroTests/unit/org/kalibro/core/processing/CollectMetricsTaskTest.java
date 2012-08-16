package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.core.settings.ServerSettings;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class CollectMetricsTaskTest extends KalibroTestCase {

	private BaseTool baseTool;
	private ProjectResult projectResult;

	private CollectMetricsTask collectTask;

	@Before
	public void setUp() {
		baseTool = analizoStub();
		projectResult = new ProjectResult(helloWorld());
		mockKalibro();
		collectTask = new CollectMetricsTask(projectResult);
	}

	private void mockKalibro() {
		KalibroSettings settings = mock(KalibroSettings.class);
		mockStatic(Kalibro.class);
		when(Kalibro.currentSettings()).thenReturn(settings);
		when(settings.getServerSettings()).thenReturn(mock(ServerSettings.class));
		mockConfiguration();
		mockBaseTool();
	}

	private void mockConfiguration() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		Configuration configuration = mock(Configuration.class);
		Map<String, Set<NativeMetric>> metricsMap = new HashMap<String, Set<NativeMetric>>();
		metricsMap.put("Analizo", baseTool.getSupportedMetrics());

		when(Kalibro.getConfigurationDao()).thenReturn(configurationDao);
		when(configurationDao.getConfigurationFor(PROJECT_NAME)).thenReturn(configuration);
		when(configuration.getNativeMetrics()).thenReturn(metricsMap);
	}

	private void mockBaseTool() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		when(Kalibro.getBaseToolDao()).thenReturn(baseToolDao);
		when(baseToolDao.getBaseTool(baseTool.getName())).thenReturn(baseTool);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.COLLECTING, collectTask.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnCollectedResults() throws Exception {
		assertDeepEquals(newHelloWorldResultMap(projectResult.getDate()), collectTask.performAndGetResult());
	}
}