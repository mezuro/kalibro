package org.kalibro.client;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.VoidTask;
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

	@Test
	public void shouldThrowExceptionOnMalformedUrl() {
		assertThat(createClientWithMalformedUrl()).throwsException().withCause(MalformedURLException.class)
			.withMessage("Invalid service address: mal formed URL");
	}

	private VoidTask createClientWithMalformedUrl() {
		return new VoidTask() {

			@Override
			protected void perform() {
				new TestEndpointClient("mal formed URL");
			}
		};
	}

	@Test
	public void shoudCreateEndpointPort() {
		TestEndpoint port = mock(TestEndpoint.class);
		when(service.getPort(any(QName.class), eq(TestEndpoint.class))).thenReturn(port);

		TestEndpointClient client = new TestEndpointClient("http://localhost:8080/KalibroService/");
		assertSame(port, client.port);

		verifyCreatedService("http://localhost:8080/KalibroService/TestEndpoint/?wsdl", "TestEndpointService");
		verifyCreatedPort("TestEndpointPort");
	}

	private void verifyCreatedService(String wsdlLocation, String qName) {
		ArgumentCaptor<URL> urlCaptor = ArgumentCaptor.forClass(URL.class);
		ArgumentCaptor<QName> qNameCaptor = ArgumentCaptor.forClass(QName.class);
		verifyStatic();
		Service.create(urlCaptor.capture(), qNameCaptor.capture());
		assertEquals(wsdlLocation, urlCaptor.getValue().toExternalForm());
		assertQName(qName, qNameCaptor.getValue());
	}

	private void verifyCreatedPort(String qName) {
		ArgumentCaptor<QName> qNameCaptor = ArgumentCaptor.forClass(QName.class);
		verify(service).getPort(qNameCaptor.capture(), eq(TestEndpoint.class));
		assertQName(qName, qNameCaptor.getValue());
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