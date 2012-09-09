package org.kalibro.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class EndpointClientTest extends TestCase {

	private Service service;

	@Before
	public void setUp() {
		service = mock(Service.class);
		mockStatic(Service.class);
		when(Service.create(any(URL.class), any(QName.class))).thenReturn(service);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowExceptionOnMalformedUrl() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				new TestEndpointClient("mal formed URL");
			}
		}, "Invalid service address: mal formed URL", MalformedURLException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shoudCreateEndpointPort() {
		TestEndpoint port = mock(TestEndpoint.class);
		when(service.getPort(any(QName.class), eq(TestEndpoint.class))).thenReturn(port);

		TestEndpointClient client = new TestEndpointClient("http://localhost:8080/KalibroService/");

		ArgumentCaptor<URL> urlCaptor = ArgumentCaptor.forClass(URL.class);
		ArgumentCaptor<QName> qNameCaptor = ArgumentCaptor.forClass(QName.class);
		verifyStatic();
		Service.create(urlCaptor.capture(), qNameCaptor.capture());
		assertEquals("http://localhost:8080/KalibroService/TestEndpoint/?wsdl", urlCaptor.getValue().toExternalForm());
		assertQName("TestEndpointImplService", qNameCaptor.getValue());

		verify(service).getPort(qNameCaptor.capture(), eq(TestEndpoint.class));
		assertQName("TestEndpointImplPort", qNameCaptor.getValue());
		assertSame(port, client.port);
	}

	private void assertQName(String localPart, QName qName) {
		assertEquals("http://service.kalibro.org/", qName.getNamespaceURI());
		assertEquals(localPart, qName.getLocalPart());
	}

	private final class TestEndpointClient extends EndpointClient<TestEndpoint> {

		private TestEndpointClient(String serviceAddress) {
			super(serviceAddress, TestEndpoint.class);
		}
	}

	private interface TestEndpoint {

		void method();
	}
}