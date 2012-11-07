package org.kalibro.core.abstractentity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.BaseTool;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dto.DaoLazyLoader;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class EntityReflectorTest extends UnitTest {

	private Person person;
	private Programmer programmer;
	private NoIdentityEntity noIdentityEntity;

	@Before
	public void setUp() {
		person = loadFixture("carlos", Person.class);
		programmer = loadFixture("carlos", Programmer.class);
		noIdentityEntity = new NoIdentityEntity();
	}

	@Test
	public void shouldUnwrapLazyLoaderProxies() {
		BaseTool entity = loadFixture("inexistent", BaseTool.class);
		BaseToolDao dao = mock(BaseToolDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(dao);
		when(dao.get("")).thenReturn(entity);

		BaseTool proxy = DaoLazyLoader.createProxy(BaseToolDao.class, "get", "");
		assertNotSame(entity, proxy);
		assertSame(entity, new EntityReflector(proxy).getObject());
	}

	@Test
	public void shouldListIdentityFields() {
		assertDeepEquals(list("identityNumber"), reflector(person).listIdentityFields());
		assertDeepEquals(list("identityNumber"), reflector(programmer).listIdentityFields());
	}

	@Test
	public void withoutSpecifyingAllFieldsShouldBeIdentityFields() {
		assertDeepEquals(list("field1", "field2"), reflector(noIdentityEntity).listIdentityFields());
	}

	@Test
	public void shouldListSortingFields() {
		assertTrue(reflector(noIdentityEntity).listSortingFields().isEmpty());
		assertDeepEquals(list("name"), reflector(person).listSortingFields());
		assertDeepEquals(list("name"), reflector(programmer).listSortingFields());
	}

	@Test
	public void shouldListSortedPrintFields() {
		assertDeepEquals(list("name", "identityNumber", "relatives", "sex"), reflector(person).listPrintFields());
		assertDeepEquals(list("field1"), reflector(new NoIdentityEntity()).listPrintFields());
	}

	@Test
	public void shouldGetPrintComment() {
		assertEquals("", reflector(person).getPrintComment("identityNumber"));
		assertEquals("name comes first", reflector(person).getPrintComment("name"));
		assertEquals("", reflector(person).getPrintComment("relatives"));
	}

	private EntityReflector reflector(AbstractEntity<?> entity) {
		return new EntityReflector(entity);
	}
}