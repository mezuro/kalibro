package org.kalibro.service;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.KalibroError;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(value = DaoFactory.class, fullyQualifiedNames = "org.kalibro.service.*Impl")
public abstract class EndpointImplementorTest<// @formatter:off
	ENTITY,
	REQUEST extends DataTransferObject<ENTITY>,
	RESPONSE extends DataTransferObject<ENTITY>,
	DAO,
	IMPLEMENTOR>// @formatter:on
	extends UnitTest {

	protected ENTITY entity;
	protected REQUEST request;
	protected RESPONSE response;

	protected DAO dao;
	protected IMPLEMENTOR implementor;

	@Before
	public void setUp() throws Exception {
		mockEntity();
		mockDao();
		implementor = constructor(implementorClass(), new Class<?>[0]).newInstance();
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

	private Method findDaoMethod() throws ClassNotFoundException {
		for (Method method : DaoFactory.class.getMethods())
			if (method.getReturnType().equals(daoClass()))
				return method;
		throw new KalibroError("DaoFactory method not found for class: " + daoClass());
	}

	private Class<REQUEST> requestClass() throws ClassNotFoundException {
		return xmlClass("Request");
	}

	private Class<RESPONSE> responseClass() throws ClassNotFoundException {
		return xmlClass("Response");
	}

	private <T> Class<T> xmlClass(String side) throws ClassNotFoundException {
		try {
			return (Class<T>) Class.forName("org.kalibro.service.xml." + entityName() + "Xml");
		} catch (ClassNotFoundException exception) {
			return (Class<T>) Class.forName("org.kalibro.service.xml." + entityName() + "Xml" + side);
		}
	}

	private Class<DAO> daoClass() throws ClassNotFoundException {
		return (Class<DAO>) Class.forName("org.kalibro.dao." + entityName() + "Dao");
	}

	private Class<IMPLEMENTOR> implementorClass() throws ClassNotFoundException {
		return (Class<IMPLEMENTOR>) Class.forName("org.kalibro.service." + entityName() + "EndpointImpl");
	}

	private String entityName() {
		return entityClass().getSimpleName();
	}

	protected abstract Class<ENTITY> entityClass();
}