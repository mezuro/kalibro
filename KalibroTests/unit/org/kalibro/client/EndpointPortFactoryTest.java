package org.kalibro.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ClientSettings;
import org.kalibro.UtilityClassTest;
import org.kalibro.core.concurrent.Task;
import org.kalibro.service.*;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class EndpointPortFactoryTest extends UtilityClassTest {

	private Service service;

	@Before
	public void setUp() {
		service = mock(Service.class);
		mockStatic(Service.class);
		when(Service.create(any(URL.class), any(QName.class))).thenReturn(service);
	}

	@Override
	protected Class<?> utilityClass() {
		return EndpointPortFactory.class;
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testMalformedUrl() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				EndpointPortFactory.getEndpointPort("mal formed URL", KalibroEndpoint.class);
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
		String serviceAddress = new ClientSettings().getServiceAddress();
		EndpointPortFactory.getEndpointPort(serviceAddress, endpointClass);

		String endpointName = endpointClass.getSimpleName();
		ArgumentCaptor<URL> urlCaptor = ArgumentCaptor.forClass(URL.class);
		ArgumentCaptor<QName> qNameCaptor = ArgumentCaptor.forClass(QName.class);

		verifyStatic();
		Service.create(urlCaptor.capture(), qNameCaptor.capture());
		assertEquals(serviceAddress + endpointName + "/?wsdl", urlCaptor.getValue().toExternalForm());
		assertEquals(endpointName + "ImplService", qNameCaptor.getValue().getLocalPart());

		verify(service).getPort(qNameCaptor.capture(), eq(endpointClass));
		assertEquals(endpointName + "ImplPort", qNameCaptor.getValue().getLocalPart());
	}
}