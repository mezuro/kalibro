package org.kalibro.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.tests.UnitTest;

public abstract class ConcreteDtoTest<ENTITY> extends UnitTest {

	private Object dto;
	private ENTITY entity;

	protected FieldReflector dtoReflector, entityReflector;

	@Before
	public void setUp() throws Exception {
		entity = loadFixture();
		dto = dtoClass().getDeclaredConstructor(entity.getClass()).newInstance(entity);
		dtoReflector = new FieldReflector(dto);
		entityReflector = new FieldReflector(entity);
	}

	protected abstract ENTITY loadFixture();

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<?> constructor = dtoClass().getConstructor();
		Modifier.isPublic(constructor.getModifiers());
		constructor.newInstance();
	}

	@Test
	public void shouldRetrieveEntityFieldsAsTheyWere() throws Exception {
		for (Method method : dtoClass().getDeclaredMethods())
			verifyField(method);
	}

	protected Class<?> dtoClass() throws ClassNotFoundException {
		return Class.forName(getClass().getName().replace("Test", ""));
	}

	private void verifyField(Method method) throws Exception {
		String methodName = method.getName();
		if (entityReflector.listFields().contains(methodName))
			assertDeepEquals(entityReflector.get(methodName), method.invoke(dto));
	}

	protected String entityName() {
		return entity.getClass().getSimpleName();
	}
}