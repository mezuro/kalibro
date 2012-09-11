package org.kalibro.dto;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.util.reflection.FieldReflector;

public abstract class ConcreteDtoTest<ENTITY, DTO extends DataTransferObject<ENTITY>> extends TestCase {

	protected DTO dto;
	protected ENTITY entity;

	@Before
	public void createFixtureAndDto() throws Exception {
		entity = loadFixture();
		dto = dtoClass().getDeclaredConstructor(entity.getClass()).newInstance(entity);
	}

	protected abstract ENTITY loadFixture();

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<DTO> constructor = dtoClass().getConstructor();
		Modifier.isPublic(constructor.getModifiers());
		assertEquals(0, constructor.getParameterTypes().length);
		constructor.newInstance();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveEntityFieldsAsTheyWere() throws Exception {
		FieldReflector reflector = new FieldReflector(entity);
		for (Method method : dtoClass().getDeclaredMethods())
			verifyField(reflector, method);
	}

	private void verifyField(FieldReflector reflector, Method method) throws Exception {
		String methodName = method.getName();
		if (reflector.listFields().contains(methodName)) {
			method.setAccessible(true);
			assertEquals(reflector.get(methodName), method.invoke(dto));
		}
	}

	protected abstract Class<DTO> dtoClass();
}