package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.ProjectResultFixtures.newHelloWorldResult;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.dao.ProjectResultDao;

@RunWith(value = Parameterized.class)
public class ProjectResultEndpointTest extends EndpointTest<ProjectResult, ProjectResultDao, ProjectResultEndpoint> {

	private static final String PROJECT_NAME = "ProjectResultEndpointTest project name";
	private static final Date DATE = new Date();

	@Parameters
	public static Collection<Boolean[]> flags() {
		return Arrays.asList(new Boolean[]{true}, new Boolean[]{false});
	}

	private boolean flag;

	public ProjectResultEndpointTest(boolean flag) {
		this.flag = flag;
	}

	@Override
	protected ProjectResult loadFixture() {
		return newHelloWorldResult(DATE);
	}

	@Test
	public void testHasResultsFor() {
		when(dao.hasResultsFor(PROJECT_NAME)).thenReturn(flag);
		assertEquals(flag, port.hasResultsFor(PROJECT_NAME));
	}

	@Test
	public void testHasResultsBefore() {
		when(dao.hasResultsBefore(DATE, PROJECT_NAME)).thenReturn(flag);
		assertEquals(flag, port.hasResultsBefore(DATE, PROJECT_NAME));
	}

	@Test
	public void testHasResultsAfter() {
		when(dao.hasResultsAfter(DATE, PROJECT_NAME)).thenReturn(flag);
		assertEquals(flag, port.hasResultsAfter(DATE, PROJECT_NAME));
	}

	@Test
	public void testFirstResult() {
		when(dao.getFirstResultOf(PROJECT_NAME)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getFirstResultOf(PROJECT_NAME));
	}

	@Test
	public void testLastResult() {
		when(dao.getLastResultOf(PROJECT_NAME)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getLastResultOf(PROJECT_NAME));
	}

	@Test
	public void testLastResultBefore() {
		when(dao.getLastResultBefore(DATE, PROJECT_NAME)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getLastResultBefore(DATE, PROJECT_NAME));
	}

	@Test
	public void testFirstResultAfter() {
		when(dao.getFirstResultAfter(DATE, PROJECT_NAME)).thenReturn(entity);
		assertDeepDtoEquals(entity, port.getFirstResultAfter(DATE, PROJECT_NAME));
	}
}