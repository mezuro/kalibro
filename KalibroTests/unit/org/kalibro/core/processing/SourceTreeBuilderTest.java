package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ModuleResult.class, SourceTreeBuilder.class})
public class SourceTreeBuilderTest extends UnitTest {

	private static final Long RESULT_ID = new Random().nextLong();
	private static final Long PROCESSING_ID = new Random().nextLong();
	private static final String REPOSITORY_NAME = "SourceTreeBuilderTest repository name";

	private ModuleResultDatabaseDao dao;
	private ModuleResult result;

	private SourceTreeBuilder treeBuilder;

	@Before
	public void setUp() {
		dao = mock(ModuleResultDatabaseDao.class);
		result = mock(ModuleResult.class);
		when(result.getId()).thenReturn(RESULT_ID);
		when(dao.save(result, PROCESSING_ID)).thenReturn(result);
		treeBuilder = new SourceTreeBuilder(PROCESSING_ID, REPOSITORY_NAME, dao);
	}

	@Test
	public void shouldSaveRootWithRepositoryName() throws Exception {
		Module rootModule = new Module(SOFTWARE, REPOSITORY_NAME);
		whenNew(ModuleResult.class).withArguments(null, rootModule).thenReturn(result);

		assertEquals(RESULT_ID, treeBuilder.save(new Module(SOFTWARE, "null")));
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldAppendAncestryToLastExistingAncestor() throws Exception {
		Module junit = new Module(PACKAGE, "org", "junit");
		Module org = junit.inferParent();
		Module root = org.inferParent();
		ModuleResult rootResult = new ModuleResult(null, root);
		ModuleResult orgResult = new ModuleResult(rootResult, org);

		when(dao.getResultFor(root, PROCESSING_ID)).thenReturn(rootResult);
		whenNew(ModuleResult.class).withArguments(rootResult, org).thenReturn(orgResult);
		when(dao.save(orgResult, PROCESSING_ID)).thenReturn(orgResult);
		whenNew(ModuleResult.class).withArguments(orgResult, junit).thenReturn(result);

		assertEquals(RESULT_ID, treeBuilder.save(junit));
		verify(dao, never()).save(rootResult, PROCESSING_ID);
		verify(dao).save(orgResult, PROCESSING_ID);
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldChangeGranularityOfExistingModuleResult() {
		Module newModule = new Module(CLASS, "org", "junit", "Test");
		Module existingModule = mock(Module.class);
		when(dao.getResultFor(newModule, PROCESSING_ID)).thenReturn(result);
		when(result.getModule()).thenReturn(existingModule);
		when(existingModule.getGranularity()).thenReturn(PACKAGE);

		assertEquals(RESULT_ID, treeBuilder.save(newModule));
		verify(existingModule).setGranularity(CLASS);
		verify(dao).save(result, PROCESSING_ID);
	}

	@Test
	public void shouldRetrieveMaximumHeight() {
		Module root = new Module(SOFTWARE, REPOSITORY_NAME);
		Module leaf = new Module(CLASS, "org", "kalibro", "core", "processing", "SourceTreeBuilderTest");
		when(dao.getResultFor(root, PROCESSING_ID)).thenReturn(new ModuleResult(null, root));
		when(dao.getResultFor(leaf, PROCESSING_ID)).thenReturn(result);
		when(result.getModule()).thenReturn(leaf);
		when(result.getHeight()).thenReturn(5);

		treeBuilder.save(root);
		treeBuilder.save(leaf);
		assertEquals(5, treeBuilder.getMaximumHeight());
	}
}