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
public abstract class ClientTest<PORT, CLIENT extends EndpointClient<PORT>,
// DTO parameters
ENTITY, REQUEST extends DataTransferObject<ENTITY>, RESPONSE extends DataTransferObject<ENTITY>> extends TestCase {

	private Class<?>[] classes;

	protected ENTITY entity;
	protected REQUEST request;
	protected RESPONSE response;

	protected PORT port;
	protected CLIENT client;

	@Before
	public void setUp() throws Exception {
		classes = parameterClasses();
		mockEntity();
		createSupressedClient();
	}

	private void mockEntity() throws Exception {
		entity = mock((Class<ENTITY>) classes[2]);
		request = mock((Class<REQUEST>) classes[3]);
		response = mock((Class<RESPONSE>) classes[4]);
		when(response.convert()).thenReturn(entity);
		whenNew((Class<REQUEST>) classes[3]).withArguments(entity).thenReturn(request);
	}

	private void createSupressedClient() throws Exception {
		suppress(constructor(EndpointClient.class, String.class, Class.class));
		client = constructor((Class<CLIENT>) classes[1], String.class).newInstance("");

		port = mock((Class<PORT>) classes[0]);
		Whitebox.setInternalState(client, "port", port);
	}

	protected abstract Class<?>[] parameterClasses();
}