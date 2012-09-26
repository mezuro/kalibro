package org.kalibro.client;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import org.junit.After;
import org.junit.Before;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.tests.IntegrationTest;
import org.powermock.reflect.Whitebox;

public abstract class EndpointTest<ENTITY, DAO, ENDPOINT> extends IntegrationTest {

	private static final String SERVICE_ADDRESS = "http://localhost:8080/KalibroService/";

	protected DAO dao;
	protected ENTITY entity;
	protected ENDPOINT port;

	private Endpoint endpoint;

	@Before
	public void setUp() throws Exception {
		entity = loadFixture();
		dao = mock(daoClass());
		publish();
		port = EndpointClient.getPort(SERVICE_ADDRESS, endpointClass());
	}

	protected abstract ENTITY loadFixture();

	private void publish() throws Exception {
		Class<?> implementorClass = Class.forName(endpointClass().getName() + "Impl");
		Object implementor = implementorClass.getConstructor(daoClass()).newInstance(dao);
		endpoint = Endpoint.create(implementor);
		Logger logger = Logger.getLogger("com.sun.xml.ws.monitoring");
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.OFF);
		endpoint.publish(SERVICE_ADDRESS + endpointClass().getSimpleName() + "/");
	}

	private Class<DAO> daoClass() throws ClassNotFoundException {
		return (Class<DAO>) Class.forName("org.kalibro.dao." + entityName() + "Dao");
	}

	private Class<ENDPOINT> endpointClass() throws ClassNotFoundException {
		return (Class<ENDPOINT>) Class.forName("org.kalibro.service." + entityName() + "Endpoint");
	}

	private String entityName() {
		return entity.getClass().getSimpleName();
	}

	@After
	public void tearDown() {
		endpoint.stop();
	}

	protected <T extends DataTransferObject<ENTITY>> void assertDeepDtoEquals(ENTITY expected, T actual) {
		assertDeepEquals(expected, convert(actual));
	}

	protected void assertDeepDtoList(List<? extends DataTransferObject<ENTITY>> dtoList, ENTITY... expected) {
		assertDeepEquals(asList(expected), convert(dtoList));
	}

	private List<ENTITY> convert(Collection<? extends DataTransferObject<ENTITY>> dtos) {
		List<ENTITY> entities = new ArrayList<ENTITY>();
		for (DataTransferObject<ENTITY> dto : dtos)
			entities.add(convert(dto));
		return entities;
	}

	private ENTITY convert(DataTransferObject<ENTITY> dto) {
		ENTITY converted = dto.convert();
		for (String field : fieldsThatShouldBeProxy())
			assertProxy(converted, field);
		return converted;
	}

	protected List<String> fieldsThatShouldBeProxy() {
		return new ArrayList<String>();
	}

	private void assertProxy(ENTITY converted, String field) {
		Object value = Whitebox.getInternalState(converted, field);
		assertTrue("Field " + field + " is not a proxy.", value.getClass().getName().contains("EnhancerByCGLIB"));

		Object originalValue = Whitebox.getInternalState(entity, field);
		Whitebox.setInternalState(converted, field, originalValue);
	}
}