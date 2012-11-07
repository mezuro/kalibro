package org.kalibro.service;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.kalibro.KalibroError;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DataTransferObject;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, DataTransferObject.class})
public abstract class EndpointImplementorTest<ENTITY, XML extends DataTransferObject<ENTITY>, DAO, IMPLEMENTOR>
	extends UnitTest {

	protected XML xml;
	protected ENTITY entity;

	protected DAO dao;
	protected IMPLEMENTOR implementor;

	@Before
	public void setUp() throws Exception {
		mockEntity();
		mockDao();
		implementor = constructor(implementorClass(), new Class<?>[0]).newInstance();
	}

	private void mockEntity() throws Exception {
		xml = mock(xmlClass());
		entity = mock(entityClass());
		mockStatic(DataTransferObject.class);
		when(xml.convert()).thenReturn(entity);
		whenNew(xmlClass()).withArguments(entity).thenReturn(xml);
		when(DataTransferObject.createDtos(sortedSet(entity), xmlClass())).thenReturn(list(xml));
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

	private Class<ENTITY> entityClass() throws ClassNotFoundException {
		return (Class<ENTITY>) Class.forName("org.kalibro." + entityName());
	}

	private Class<XML> xmlClass() throws ClassNotFoundException {
		return (Class<XML>) Class.forName("org.kalibro.service.xml." + entityName() + "Xml");
	}

	private Class<DAO> daoClass() throws ClassNotFoundException {
		return (Class<DAO>) Class.forName("org.kalibro.dao." + entityName() + "Dao");
	}

	private Class<IMPLEMENTOR> implementorClass() throws ClassNotFoundException {
		return (Class<IMPLEMENTOR>) Class.forName("org.kalibro.service." + entityName() + "EndpointImpl");
	}

	private String entityName() {
		return getClass().getSimpleName().replace("EndpointImplTest", "");
	}
}