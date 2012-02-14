package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleFixtures.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleNode;
import org.kalibro.core.model.ProjectResult;

public class SourceTreeBuilderTest extends KalibroTestCase {

	private ProjectResult projectResult;
	private SourceTreeBuilder treeBuilder;

	@Before
	public void setUp() {
		projectResult = helloWorldResult();
		projectResult.setSourceTree(null);
		treeBuilder = new SourceTreeBuilder(projectResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkSourceTree() {
		ModuleNode sourceTree = getSourceTree(
			new Module(CLASS, "org.junit.Assert"),
			new Module(CLASS, "org.junit.ComparisonFailure"),
			new Module(PACKAGE, "org.junit"),
			new Module(CLASS, "org.analizo.AnalizoMetricCollector"),
			new Module(CLASS, "org.analizo.AnalizoOutputParser"));

		String projectName = projectResult.getProject().getName();
		assertDeepEquals(new Module(APPLICATION, projectName), sourceTree.getModule());
		assertEquals(1, sourceTree.getChildren().size());
		assertDeepEquals(junitAnalizoTree(), sourceTree.getChildren().iterator().next());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetRootNameAsProjectName() {
		assertDeepEquals(helloWorldTree(), getSourceTree(helloWorldApplication(), helloWorldClass()));
	}

	private ModuleNode getSourceTree(Module... modules) {
		assertNull(projectResult.getSourceTree());
		treeBuilder.buildSourceTree(Arrays.asList(modules));
		return projectResult.getSourceTree();
	}
}