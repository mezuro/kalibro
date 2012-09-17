package org.kalibro.client;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.dto.DataTransferObject;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ReadingClientDao.class, ReadingGroupClientDao.class, EndpointClient.class})
public abstract class ClientTest<// @formatter:off
	ENTITY,
	REQUEST extends DataTransferObject<ENTITY>,
	RESPONSE extends DataTransferObject<ENTITY>,
	ENDPOINT,
	CLIENT extends EndpointClient<ENDPOINT>>// @formatter:on
	extends TestCase {

	private Class<?>[] classes;

	protected ENTITY entity;
	protected REQUEST request;
	protected RESPONSE response;

	protected ENDPOINT port;
	protected CLIENT client;

	@Before
	public void setUp() throws Exception {
		classes = parameterClasses();
		mockEntity();
		createSupressedClient();
	}

	private void mockEntity() throws Exception {
		entity = mock(entityClass());
		request = mock(requestClass());
		response = mock(responseClass());
		when(response.convert()).thenReturn(entity);
		whenNew(requestClass()).withArguments(entity).thenReturn(request);
	}

	private void createSupressedClient() throws Exception {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		client = constructor(clientClass(), String.class).newInstance("");

		port = mock(endpointClass());
		Whitebox.setInternalState(client, "port", port);
	}

	protected abstract Class<?>[] parameterClasses();

	private Class<ENTITY> entityClass() {
		return (Class<ENTITY>) classes[0];
	}

	private Class<REQUEST> requestClass() {
		return (Class<REQUEST>) classes[1];
	}

	private Class<RESPONSE> responseClass() {
		return (Class<RESPONSE>) classes[2];
	}

	private Class<ENDPOINT> endpointClass() {
		return (Class<ENDPOINT>) classes[3];
	}

	private Class<CLIENT> clientClass() {
		return (Class<CLIENT>) classes[4];
	}
}