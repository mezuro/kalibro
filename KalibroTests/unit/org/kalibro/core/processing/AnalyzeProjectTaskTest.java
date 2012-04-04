package org.kalibro.core.processing;

import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.Configuration;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.BaseToolDao;
import org.kalibro.core.persistence.dao.ConfigurationDao;
import org.kalibro.core.settings.KalibroSettings;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class AnalyzeProjectTaskTest extends KalibroTestCase {

	private ProjectResult projectResult;
	private Configuration configuration;

	private AnalyzeProjectTask analyzeTask;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		projectResult.setSourceTree(null);
		configuration = newConfiguration("cbo", "lcom4");
		mockKalibro();
		analyzeTask = new AnalyzeProjectTask(projectResult);
		analyzeTask.executeAndWait();
	}

	private void mockKalibro() {
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		BaseToolDao baseToolDao = PowerMockito.mock(BaseToolDao.class);
		ConfigurationDao configurationDao = PowerMockito.mock(ConfigurationDao.class);

		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
		PowerMockito.when(Kalibro.getBaseToolDao()).thenReturn(baseToolDao);
		PowerMockito.when(Kalibro.getConfigurationDao()).thenReturn(configurationDao);

		BaseTool baseTool = analizoStub();
		PowerMockito.when(baseToolDao.getBaseTool(baseTool.getName())).thenReturn(baseTool);

		String projectName = projectResult.getProject().getName();
		PowerMockito.when(configurationDao.getConfigurationFor(projectName)).thenReturn(configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetSourceTreeOnProjectResultAndModuleResultsOnTaskResult() {
		Collection<ModuleResult> results = Whitebox.getInternalState(analyzeTask, "result");

		assertDeepEquals(helloWorldRoot(), projectResult.getSourceTree());
		assertDeepEquals(newHelloWorldResults(projectResult.getDate()), results);
	}
}