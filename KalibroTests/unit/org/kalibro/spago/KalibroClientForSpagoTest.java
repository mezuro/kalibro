package org.kalibro.spago;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.*;
import org.kalibro.service.KalibroEndpoint;
import org.kalibro.service.ModuleResultEndpoint;
import org.kalibro.service.ProjectEndpoint;
import org.kalibro.service.ProjectResultEndpoint;
import org.kalibro.service.entities.ModuleResultXml;
import org.kalibro.service.entities.ProjectResultXml;
import org.kalibro.service.entities.RawProjectXml;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class KalibroClientForSpagoTest extends KalibroTestCase {

	private KalibroEndpoint kalibroPort;
	private ProjectEndpoint projectPort;
	private ModuleResultEndpoint moduleResultPort;
	private ProjectResultEndpoint projectResultPort;

	private KalibroClientForSpago client;

	@Before
	public void setUp() throws MalformedURLException {
		createMocks();
		mockService();
		client = new KalibroClientForSpago("file:///");
	}

	private void createMocks() {
		kalibroPort = mock(KalibroEndpoint.class);
		projectPort = mock(ProjectEndpoint.class);
		moduleResultPort = mock(ModuleResultEndpoint.class);
		projectResultPort = mock(ProjectResultEndpoint.class);
	}

	private void mockService() {
		Service service = mock(Service.class);
		mockStatic(Service.class);
		when(Service.create(any(URL.class), any(QName.class))).thenReturn(service);
		when(service.getPort(any(QName.class), eq(KalibroEndpoint.class))).thenReturn(kalibroPort);
		when(service.getPort(any(QName.class), eq(ProjectEndpoint.class))).thenReturn(projectPort);
		when(service.getPort(any(QName.class), eq(ModuleResultEndpoint.class))).thenReturn(moduleResultPort);
		when(service.getPort(any(QName.class), eq(ProjectResultEndpoint.class))).thenReturn(projectResultPort);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasResultsFor() {
		when(projectResultPort.hasResultsFor("")).thenReturn(true);
		assertTrue(client.hasResultsFor(""));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testSaveAndProcess() {
		Project project = ProjectFixtures.helloWorld();
		client.saveAndProcess(project);

		ArgumentCaptor<RawProjectXml> captor = ArgumentCaptor.forClass(RawProjectXml.class);
		Mockito.verify(projectPort).saveProject(captor.capture());
		assertEquals(project, captor.getValue().convert());
		Mockito.verify(kalibroPort).processProject(project.getName());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetLastApplicationResult() {
		Project project = ProjectFixtures.helloWorld();
		ProjectResult projectResult = new ProjectResult(project);
		mockProjectResult(projectResult);
		ModuleResult moduleResult = mockModuleResult(projectResult);

		assertDeepEquals(moduleResult, client.getLastApplicationResult(project.getName()));
	}

	private void mockProjectResult(ProjectResult projectResult) {
		String projectName = projectResult.getProject().getName();
		ProjectResultXml resultXml = new ProjectResultXml(projectResult);
		when(projectResultPort.getLastResultOf(projectName)).thenReturn(resultXml);
	}

	private ModuleResult mockModuleResult(ProjectResult projectResult) {
		Date date = projectResult.getDate();
		String projectName = projectResult.getProject().getName();
		ModuleResult moduleResult = ModuleResultFixtures.helloWorldApplicationResult();
		ModuleResultXml moduleResultXml = new ModuleResultXml(moduleResult);
		when(moduleResultPort.getModuleResult(projectName, projectName, date)).thenReturn(moduleResultXml);
		return moduleResult;
	}
}