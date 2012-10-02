package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.MetricCollectorStub.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.SortedSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.BaseToolDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class BaseToolTest extends UnitTest {

	private BaseTool baseTool;

	@Before
	public void setUp() {
		baseTool = new BaseTool(CLASS_NAME);
	}

	@Test
	public void shoouldGetAllBaseTools() {
		BaseToolDao dao = mock(BaseToolDao.class);
		SortedSet<BaseTool> baseTools = mock(SortedSet.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getBaseToolDao()).thenReturn(dao);
		when(dao.all()).thenReturn(baseTools);

		assertSame(baseTools, BaseTool.all());
	}

	@Test
	public void shouldSortByName() {
		assertSorted(baseTool("A"), baseTool("B"), baseTool("C"), baseTool("X"), baseTool("Y"), baseTool("Z"));
	}

	@Test
	public void shouldIdentifyByName() {
		assertEquals(baseTool, baseTool(baseTool.getName()));
	}

	private BaseTool baseTool(String name) {
		return new BaseTool(name, null, null, null);
	}

	@Test
	public void shouldHaveDefaultConstructorForYamlLoading() throws Exception {
		Constructor<BaseTool> constructor = BaseTool.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		assertDeepEquals(new BaseTool(null, null, null, null), constructor.newInstance());
	}

	@Test
	public void checkConstruction() {
		check();
		baseTool = new BaseTool(NAME, DESCRIPTION, asSet(SUPPORTED_METRIC), CLASS_NAME);
		check();
	}

	private void check() {
		assertEquals(NAME, baseTool.getName());
		assertEquals(DESCRIPTION, baseTool.getDescription());
		assertEquals(asSet(SUPPORTED_METRIC), baseTool.getSupportedMetrics());
		assertEquals(CLASS_NAME, baseTool.getCollectorClassName());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		assertEquals(asSet(RESULT), baseTool.collectMetrics(null, null));
	}

	@Test
	public void shouldThrowExceptionIfCannotCreateCollector() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				new BaseTool("invalid.Class");
			}
		}).throwsException().withMessage("Could not create metric collector: invalid.Class")
			.withCause(ClassNotFoundException.class);
	}

	@Test
	public void toStringShouldBeName() {
		assertEquals(baseTool.getName(), "" + baseTool);
	}
}