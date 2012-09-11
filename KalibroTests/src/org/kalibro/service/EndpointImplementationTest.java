package org.kalibro.service;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.KalibroError;
import org.kalibro.TestCase;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(value = DaoFactory.class, fullyQualifiedNames = "org.kalibro.service.*Impl")
public abstract class EndpointImplementationTest<// @formatter:off
	ENTITY,
	REQUEST extends DataTransferObject<ENTITY>,
	RESPONSE extends DataTransferObject<ENTITY>,
	DAO,
	ENDPOINT>// @formatter:on
	extends TestCase {

	private Class<?>[] classes;

	protected ENTITY entity;
	protected REQUEST request;
	protected RESPONSE response;

	protected DAO dao;
	protected ENDPOINT endpoint;

	@Before
	public void setUp() throws Exception {
		classes = parameterClasses();
		mockEntity();
		mockDao();
		endpoint = constructor(endpointClass(), new Class<?>[0]).newInstance();
	}

	private void mockEntity() throws Exception {
		entity = mock(entityClass());
		request = mock(requestClass());
		response = mock(responseClass());
		when(request.convert()).thenReturn(entity);
		whenNew(responseClass()).withArguments(entity).thenReturn(response);
	}

	private void mockDao() throws Exception {
		dao = mock(daoClass());
		Method method = findDaoMethod();
		mockStatic(DaoFactory.class);
		when(DaoFactory.class, method).withNoArguments().thenReturn(dao);
	}

	private Method findDaoMethod() {
		for (Method method : DaoFactory.class.getMethods())
			if (method.getReturnType().equals(daoClass()))
				return method;
		throw new KalibroError("DaoFactory method not found for class: " + daoClass());
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

	private Class<DAO> daoClass() {
		return (Class<DAO>) classes[3];
	}

	private Class<ENDPOINT> endpointClass() {
		return (Class<ENDPOINT>) classes[4];
	}
}