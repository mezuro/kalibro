package org.kalibro.service;

import static org.junit.Assert.assertEquals;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import java.util.Date;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.kalibro.ProjectResult;
import org.kalibro.client.EndpointTest;
import org.kalibro.dao.ProjectResultDao;

@RunWith(value = Theories.class)
public class ProjectResultEndpointTest extends EndpointTest<ProjectResult, ProjectResultDao, ProjectResultEndpoint> {

	private static final String PROJECT_NAME = "ProjectResultEndpointTest project name";
	private static final Date DATE = new Date();

	@DataPoints
	public static boolean[] flags() {
		return new boolean[]{true, false};
	}

	@Override
	protected ProjectResult loadFixture() {
		return newHelloWorldResult(DATE);
	}

	@Theory
	public void testHasResultsFor(boolean flag) {
		when(dao.hasResultsFor(PROJECT_NAME)).thenReturn(flag);
		assertEquals(flag, port.hasResultsFor(PROJECT_NAME));
	}

	@Theory
	public void testHasResultsBefore(boolean flag) {
		when(dao.hasResultsBefore(DATE, PROJECT_NAME)).thenReturn(flag);
		assertEquals(flag, port.hasResultsBefore(DATE, PROJECT_NAME));
	}

	@Theory
	public void testHasResultsAfter(boolean flag) {
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