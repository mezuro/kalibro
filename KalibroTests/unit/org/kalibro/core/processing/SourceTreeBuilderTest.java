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
import org.kalibro.Processing;
import org.kalibro.tests.UnitTest;

public class SourceTreeBuilderTest extends UnitTest {

	private Processing processing;
	private SourceTreeBuilder treeBuilder;

	@Before
	public void setUp() {
		processing = newHelloWorldResult();
		processing.setResultsRoot(null);
		treeBuilder = new SourceTreeBuilder(processing);
	}

	@Test
	public void checkSourceTree() {
		ModuleNode sourceTree = getSourceTree(
			new Module(CLASS, "org.checkstyle.CheckstyleMetricCollector"),
			new Module(CLASS, "org.checkstyle.CheckstyleOutputParser"),
			new Module(PACKAGE, "org.checkstyle"),
			new Module(CLASS, "org.analizo.AnalizoMetricCollector"),
			new Module(CLASS, "org.analizo.AnalizoResultParser"));

		String projectName = processing.getRepository().getName();
		assertDeepEquals(new Module(SOFTWARE, projectName), sourceTree.getModule());
		assertEquals(1, sourceTree.getChildren().size());
		assertDeepEquals(analizoCheckstyleTree(), sourceTree.getChildren().iterator().next());
	}

	@Test
	public void shouldSetRootNameAsProjectName() {
		assertDeepEquals(helloWorldRoot(), getSourceTree(helloWorldApplication(), helloWorldClass()));
	}

	private ModuleNode getSourceTree(Module... modules) {
		assertFalse(processing.isProcessed());
		treeBuilder.buildSourceTree(asList(modules));
		return processing.getResultsRoot();
	}
}