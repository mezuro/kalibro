package org.kalibro;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.kalibro.dto.DataTransferObject;
import org.powermock.reflect.Whitebox;

public abstract class DtoTestCase<ENTITY, RECORD extends DataTransferObject<ENTITY>> extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultConstructorShouldDoNothing() throws Exception {
		RECORD record = newDtoUsingDefaultConstructor();
		for (Field field : record.getClass().getDeclaredFields())
			if (isInstanceField(field))
				assertNull(field.getName(), Whitebox.getInternalState(record, field.getName()));
	}

	private boolean isInstanceField(Field field) {
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		boolean isOuterField = field.getName().startsWith("this$");
		return ! (isStatic || isOuterField);
	}

	protected abstract RECORD newDtoUsingDefaultConstructor();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkCorrectConversion() {
		for (ENTITY entity : entitiesForTestingConversion())
			assertCorrectConversion(entity, createDto(entity).convert());
	}

	protected abstract Collection<ENTITY> entitiesForTestingConversion();

	protected abstract RECORD createDto(ENTITY entity);

	protected void assertCorrectConversion(ENTITY original, ENTITY converted) {
		assertDeepEquals(Arrays.asList(original), Arrays.asList(converted));
	}
}