package org.kalibro.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoLazyLoader.class)
public abstract class AbstractDtoTest<ENTITY, DTO extends DataTransferObject<ENTITY>> extends UnitTest {

	protected DTO dto;
	protected ENTITY entity;

	@Before
	public void setUp() throws Exception {
		entity = loadFixture();
		dto = createDtoStub();
		mockStatic(DaoLazyLoader.class);
		for (LazyLoadExpectation e : lazyLoadExpectations())
			when(DaoLazyLoader.createProxy(eq(e.daoClass), eq(e.methodName), parameters(e))).thenReturn(e.stub);
	}

	protected abstract ENTITY loadFixture();

	private DTO createDtoStub() throws Exception {
		Class<?> entityClass = entity.getClass();
		Class<?> stubClass = Class.forName("org.kalibro.dto." + entityClass.getSimpleName() + "DtoStub");
		return (DTO) stubClass.getDeclaredConstructor(entityClass).newInstance(entity);
	}

	@Test
	public void shouldConvert() {
		assertDeepEquals(entity, dto.convert());
		for (LazyLoadExpectation e : lazyLoadExpectations()) {
			verifyStatic();
			DaoLazyLoader.createProxy(eq(e.daoClass), eq(e.methodName), parameters(e));
		}
	}

	protected List<LazyLoadExpectation> lazyLoadExpectations() {
		return new ArrayList<LazyLoadExpectation>();
	}

	private Object parameters(LazyLoadExpectation expectation) {
		return argThat(new ParametersMatcher(expectation));
	}

	protected LazyLoadExpectation expectLazy(Object stub, Class<?> daoClass, String methodName, Object... parameters) {
		return new LazyLoadExpectation(stub, daoClass, methodName, parameters);
	}
}