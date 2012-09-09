package org.kalibro.desktop.swingextension.renderer;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;

public class TreeRendererTest extends TestCase {

	private JTree tree;
	private TreeRenderer renderer;

	@Before
	public void setUp() {
		tree = new JTree();
		renderer = new TreeRenderer();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderText() {
		assertEquals("", renderValue(null).getText());
		assertEquals("My string", renderValue("My string").getText());
		assertEquals("42", renderValue(42).getText());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void defaultBackgroundShouldBeTreeBackground() {
		Color background = new Color(new Random(System.currentTimeMillis()).nextInt());
		tree.setBackground(background);
		assertEquals(background, renderValue("").getBackground());
	}

	private JLabel renderValue(Object value) {
		return renderer.getTreeCellRendererComponent(tree, value, false, false, false, 0, false);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderLeafIconForLeaf() {
		Icon leafIcon = new DefaultTreeCellRenderer().getLeafIcon();
		assertEquals(leafIcon, renderIconForTree(true, true).getIcon());
		assertEquals(leafIcon, renderIconForTree(true, false).getIcon());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderExpandedIconForExpanded() {
		assertEquals(new DefaultTreeCellRenderer().getOpenIcon(), renderIconForTree(false, true).getIcon());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRenderClosedIconForClosed() {
		assertEquals(new DefaultTreeCellRenderer().getClosedIcon(), renderIconForTree(false, false).getIcon());
	}

	private JLabel renderIconForTree(boolean isLeaf, boolean isExpanded) {
		return renderer.getTreeCellRendererComponent(tree, null, false, isExpanded, isLeaf, 0, false);
	}
}