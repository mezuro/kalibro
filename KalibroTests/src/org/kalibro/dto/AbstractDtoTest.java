package org.kalibro.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.reflection.FieldReflector;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public abstract class AbstractDtoTest<ENTITY> extends UnitTest {

	protected ENTITY entity;
	protected DataTransferObject<ENTITY> dto;

	private List<LazyLoadExpectation> lazyLoadExpectations;

	@Before
	public void setUp() throws Exception {
		entity = loadFixture();
		createDto();
		mockLazyLoading();
	}

	protected abstract ENTITY loadFixture();

	private void createDto() throws Exception {
		Class<?> dtoClass = Class.forName(getClass().getName().replace("Test", ""));
		dto = (DataTransferObject<ENTITY>) mock(dtoClass, Mockito.CALLS_REAL_METHODS);
		FieldReflector reflector = new FieldReflector(entity);
		for (Method method : dtoClass.getDeclaredMethods())
			if (Modifier.isAbstract(method.getModifiers()) && reflector.listFields().contains(method.getName()))
				doReturn(reflector.get(method.getName())).when(dto, method).withNoArguments();
	}

	private void mockLazyLoading() throws Exception {
		lazyLoadExpectations = new ArrayList<LazyLoadExpectation>();
		registerLazyLoadExpectations();
		mockStatic(DaoLazyLoader.class);
		for (LazyLoadExpectation e : lazyLoadExpectations)
			when(DaoLazyLoader.createProxy(eq(e.daoClass), eq(e.methodName), parameters(e))).thenReturn(e.returnValue);
	}

	@SuppressWarnings("unused" /* to be overriden */)
	protected void registerLazyLoadExpectations() throws Exception {
		return;
	}

	protected LazyLoadExpectation whenLazy(Class<?> daoClass, String methodName, Object... parameters) {
		LazyLoadExpectation expectation = new LazyLoadExpectation(daoClass, methodName, parameters);
		lazyLoadExpectations.add(expectation);
		return expectation;
	}

	@Test
	public void shouldHavePublicDefaultConstructor() throws Exception {
		Constructor<?> constructor = dto.getClass().getConstructor();
		Modifier.isPublic(constructor.getModifiers());
		constructor.newInstance();
	}

	@Test
	public void shouldConvert() {
		assertDeepEquals(entity, dto.convert());
		for (LazyLoadExpectation e : lazyLoadExpectations) {
			verifyStatic();
			DaoLazyLoader.createProxy(eq(e.daoClass), eq(e.methodName), parameters(e));
		}
	}

	private Object parameters(LazyLoadExpectation expectation) {
		return argThat(new ParametersMatcher(expectation));
	}
}