package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.Granularity.*;
import static org.kalibro.ModuleFixtures.*;
import static org.kalibro.ModuleNodeFixtures.*;
import static org.kalibro.ProjectResultFixtures.newHelloWorldResult;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Module;
import org.kalibro.ModuleNode;
import org.kalibro.ProjectResult;
import org.kalibro.tests.UnitTest;

public class SourceTreeBuilderTest extends UnitTest {

	private ProjectResult projectResult;
	private SourceTreeBuilder treeBuilder;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		projectResult.setSourceTree(null);
		treeBuilder = new SourceTreeBuilder(projectResult);
	}

	@Test
	public void checkSourceTree() {
		ModuleNode sourceTree = getSourceTree(
			new Module(CLASS, "org.checkstyle.CheckstyleMetricCollector"),
			new Module(CLASS, "org.checkstyle.CheckstyleOutputParser"),
			new Module(PACKAGE, "org.checkstyle"),
			new Module(CLASS, "org.analizo.AnalizoMetricCollector"),
			new Module(CLASS, "org.analizo.AnalizoOutputParser"));

		String projectName = projectResult.getProject().getName();
		assertDeepEquals(new Module(SOFTWARE, projectName), sourceTree.getModule());
		assertEquals(1, sourceTree.getChildren().size());
		assertDeepEquals(analizoCheckstyleTree(), sourceTree.getChildren().iterator().next());
	}

	@Test
	public void shouldSetRootNameAsProjectName() {
		assertDeepEquals(helloWorldRoot(), getSourceTree(helloWorldApplication(), helloWorldClass()));
	}

	private ModuleNode getSourceTree(Module... modules) {
		assertFalse(projectResult.isProcessed());
		treeBuilder.buildSourceTree(asList(modules));
		return projectResult.getSourceTree();
	}
}