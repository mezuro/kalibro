package org.kalibro.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.settings.KalibroSettings;
import org.kalibro.service.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, Service.class})
public class EndpointPortFactoryTest extends KalibroTestCase {

	@BeforeClass
	public static void emmaCoverage() throws Exception {
		Constructor<EndpointPortFactory> constructor = EndpointPortFactory.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	private Service service;
	private KalibroSettings settings;

	@Before
	public void setUp() {
		mockSettings();
		mockService();
	}

	private void mockSettings() {
		settings = new KalibroSettings();
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
	}

	private void mockService() {
		service = PowerMockito.mock(Service.class);
		PowerMockito.mockStatic(Service.class);
		PowerMockito.when(Service.create(any(URL.class), any(QName.class))).thenReturn(service);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testMalformedUrl() {
		settings.getClientSettings().setServiceAddress("mal formed URL");
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				EndpointPortFactory.getEndpointPort(KalibroEndpoint.class);
			}
		}, "Invalid service address: mal formed URL", MalformedURLException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testKalibroEndpointPort() {
		testEndpointPort(KalibroEndpoint.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testBaseToolEndpointPort() {
		testEndpointPort(BaseToolEndpoint.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConfigurationEndpointPort() {
		testEndpointPort(ConfigurationEndpoint.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectEndpointPort() {
		testEndpointPort(ProjectEndpoint.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testProjectResultEndpointPort() {
		testEndpointPort(ProjectResultEndpoint.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testModuleResultEndpointPort() {
		testEndpointPort(ModuleResultEndpoint.class);
	}

	private void testEndpointPort(Class<?> endpointClass) {
		EndpointPortFactory.getEndpointPort(endpointClass);

		String endpointName = endpointClass.getSimpleName();
		ArgumentCaptor<URL> urlCaptor = ArgumentCaptor.forClass(URL.class);
		ArgumentCaptor<QName> qNameCaptor = ArgumentCaptor.forClass(QName.class);

		PowerMockito.verifyStatic();
		Service.create(urlCaptor.capture(), qNameCaptor.capture());
		String serviceAddress = settings.getClientSettings().getServiceAddress();
		assertEquals(serviceAddress + endpointName + "/?wsdl", urlCaptor.getValue().toExternalForm());
		assertEquals(endpointName + "ImplService", qNameCaptor.getValue().getLocalPart());

		Mockito.verify(service).getPort(qNameCaptor.capture(), Matchers.eq(endpointClass));
		assertEquals(endpointName + "ImplPort", qNameCaptor.getValue().getLocalPart());
	}
}