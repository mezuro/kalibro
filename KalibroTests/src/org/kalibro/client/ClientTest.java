package org.kalibro.client;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(EndpointClient.class)
public abstract class ClientTest// @formatter:off
	<ENTITY, XML extends DataTransferObject<ENTITY>, ENDPOINT, CLIENT extends EndpointClient<ENDPOINT>>// @formatter:on
	extends UnitTest {

	protected XML xml;
	protected ENTITY entity;

	protected ENDPOINT port;
	protected CLIENT client;

	@Before
	public void setUp() throws Exception {
		mockEntity();
		createSupressedClient();
	}

	private void mockEntity() throws Exception {
		xml = mock(xmlClass());
		entity = mock(entityClass());
		when(xml.convert()).thenReturn(entity);
		whenNew(xmlClass()).withArguments(entity).thenReturn(xml);
	}

	private void createSupressedClient() throws Exception {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		client = constructor(clientClass(), String.class).newInstance("");

		port = mock(endpointClass());
		Whitebox.setInternalState(client, "port", port);
	}

	private Class<XML> xmlClass() throws ClassNotFoundException {
		return (Class<XML>) Class.forName("org.kalibro.service.xml." + entityName() + "Xml");
	}

	private Class<CLIENT> clientClass() throws ClassNotFoundException {
		return (Class<CLIENT>) Class.forName("org.kalibro.client." + entityName() + "ClientDao");
	}

	private Class<ENDPOINT> endpointClass() throws ClassNotFoundException {
		return (Class<ENDPOINT>) Class.forName("org.kalibro.service." + entityName() + "Endpoint");
	}

	private String entityName() {
		return entityClass().getSimpleName();
	}

	protected abstract Class<ENTITY> entityClass();
}