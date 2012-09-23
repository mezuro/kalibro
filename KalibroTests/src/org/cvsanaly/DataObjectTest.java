package org.cvsanaly;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;
import org.kalibro.core.Identifier;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;

public abstract class DataObjectTest<T extends Object> extends UnitTest {

	@Test
	public void testDataObject() throws Exception {
		for (Field field : getTypeClass().getDeclaredFields()) {
			if (field.getAnnotations().length == 0)
				continue;
			Method getter, setter;
			T testObject = getTypeClass().newInstance();
			String fieldName = Identifier.fromVariable(field.getName()).asClassName();
			getter = getTypeClass().getDeclaredMethod("get" + fieldName);
			setter = getTypeClass().getDeclaredMethod("set" + fieldName, field.getType());
			
			Object testValue = getTestValue(field.getType());
			setter.invoke(testObject, testValue);
			assertEquals(testValue, getter.invoke(testObject));
		}
	}

	private Object getTestValue(Class<?> fieldType) {
		Object toReturn;
		if (fieldType.equals(Long.TYPE))
			toReturn = new Long(42);
		else if (fieldType.equals(Double.TYPE))
			toReturn = new Double(42);
		else if (fieldType.equals(Integer.TYPE))
			toReturn = new Integer(42);
		else if (fieldType.equals(String.class))
			toReturn = "42";
		else 
		toReturn = Mockito.mock(fieldType);
		return toReturn;
	}

	protected abstract Class<T> getTypeClass();

}
