package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.BaseToolFixtures.analizoStub;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldResultMap;
import static org.kalibro.core.model.ProjectFixtures.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroSettings;
import org.kalibro.ServerSettings;
import org.kalibro.TestCase;
import org.kalibro.core.dao.BaseToolDao;
import org.kalibro.core.dao.ConfigurationDao;
import org.kalibro.core.dao.DaoFactory;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, KalibroSettings.class})
public class CollectMetricsTaskTest extends TestCase {

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
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(settings);
		when(settings.getServerSettings()).thenReturn(mock(ServerSettings.class));
		mockConfiguration();
		mockBaseTool();
	}

	private void mockConfiguration() {
		ConfigurationDao configurationDao = mock(ConfigurationDao.class);
		Configuration configuration = mock(Configuration.class);
		Map<String, Set<NativeMetric>> metricsMap = new HashMap<String, Set<NativeMetric>>();
		metricsMap.put("Analizo", baseTool.getSupportedMetrics());

		mockStatic(DaoFactory.class);
		when(DaoFactory.getConfigurationDao()).thenReturn(configurationDao);
		when(configurationDao.getConfigurationFor(PROJECT_NAME)).thenReturn(configuration);
		when(configuration.getNativeMetrics()).thenReturn(metricsMap);
	}

	private void mockBaseTool() {
		BaseToolDao baseToolDao = mock(BaseToolDao.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(baseToolDao);
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