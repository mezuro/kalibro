package org.kalibro.client;

import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Service.class)
public class EndpointClientTest extends UnitTest {

	private Service service;
	private TestEndpoint port;

	@Before
	public void setUp() {
		service = mock(Service.class);
		port = mock(TestEndpoint.class);
		mockStatic(Service.class);
		when(Service.create(wsdlLocation(), qName("TestEndpointService"))).thenReturn(service);
		when(service.getPort(qName("TestEndpointPort"), eq(TestEndpoint.class))).thenReturn(port);
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
		TestEndpointClient client = new TestEndpointClient("http://localhost:8080/KalibroService/");
		assertSame(port, client.port);
	}

	private URL wsdlLocation() {
		return argThat(new ArgumentMatcher<URL>() {

			@Override
			public boolean matches(Object argument) {
				String url = ((URL) argument).toExternalForm();
				return url.equals("http://localhost:8080/KalibroService/TestEndpoint/?wsdl");
			}
		});
	}

	private QName qName(final String localPart) {
		return argThat(new ArgumentMatcher<QName>() {

			@Override
			public boolean matches(Object argument) {
				QName qName = (QName) argument;
				return qName.getNamespaceURI().equals("http://service.kalibro.org/")
					&& qName.getLocalPart().equals(localPart);
			}
		});
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