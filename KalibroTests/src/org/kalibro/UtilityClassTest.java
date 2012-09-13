package org.kalibro;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.junit.Test;

public abstract class UtilityClassTest extends TestCase {

	@Test
	public void shouldHavePrivateConstructor() throws Exception {
		Constructor<?> constructor = utilityClass().getDeclaredConstructor();
		assertFalse(constructor.isAccessible());
		invokeForCoverage(constructor);
	}

	private void invokeForCoverage(Constructor<?> constructor) throws Exception {
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	protected abstract Class<?> utilityClass();
}