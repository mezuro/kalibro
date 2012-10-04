package org.kalibro.client;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.kalibro.Processing;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.service.ProjectResultEndpoint;
import org.kalibro.service.xml.ProjectResultXml;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;

@PrepareOnlyThisForTest(ProjectResultClientDao.class)
public class ProjectResultClientDaoTest extends
	ClientTest<Processing, ProjectResultXml, ProjectResultXml, ProjectResultEndpoint, ProjectResultClientDao> {

	private static final boolean HAS_RESULTS = new Random().nextBoolean();
	private static final String PROJECT_NAME = "ProjectResultClientDaoTest project name";
	private static final Date DATE = new Date();

	@Override
	protected Class<Processing> entityClass() {
		return Processing.class;
	}

	@Test
	public void shouldNotSaveResultRemotely() {
		assertThat(save()).throwsException().withMessage("Cannot save project result remotely");
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				client.save(entity);
			}
		};
	}

	@Test
	public void testHasResultsFor() {
		when(port.hasResultsFor(PROJECT_NAME)).thenReturn(HAS_RESULTS);
		assertEquals(HAS_RESULTS, client.hasResultsFor(PROJECT_NAME));
	}

	@Test
	public void testHasResultsBefore() {
		when(port.hasResultsBefore(DATE, PROJECT_NAME)).thenReturn(HAS_RESULTS);
		assertEquals(HAS_RESULTS, client.hasResultsBefore(DATE, PROJECT_NAME));
	}

	@Test
	public void testHasResultsAfter() {
		when(port.hasResultsAfter(DATE, PROJECT_NAME)).thenReturn(HAS_RESULTS);
		assertEquals(HAS_RESULTS, client.hasResultsAfter(DATE, PROJECT_NAME));
	}

	@Test
	public void testGetFirstResultOf() {
		when(port.getFirstResultOf(PROJECT_NAME)).thenReturn(response);
		assertSame(entity, client.getFirstResultOf(PROJECT_NAME));
	}

	@Test
	public void testGetLastResultOf() {
		PowerMockito.when(port.getLastResultOf(PROJECT_NAME)).thenReturn(response);
		assertSame(entity, client.getLastResultOf(PROJECT_NAME));
	}

	@Test
	public void testGetLastResultBefore() {
		PowerMockito.when(port.getLastResultBefore(DATE, PROJECT_NAME)).thenReturn(response);
		assertSame(entity, client.getLastResultBefore(DATE, PROJECT_NAME));
	}

	@Test
	public void testGetFirstResultAfter() {
		PowerMockito.when(port.getFirstResultAfter(DATE, PROJECT_NAME)).thenReturn(response);
		assertSame(entity, client.getFirstResultAfter(DATE, PROJECT_NAME));
	}
}