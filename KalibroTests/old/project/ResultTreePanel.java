package org.kalibro.desktop.old.project;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.kalibro.desktop.old.model.ResultTreeCellRenderer;
import org.kalibro.desktop.old.model.SourceTreeNode;

public class ResultTreePanel extends JPanel implements TreeExpansionListener {

	private JTree tree;
	private DefaultTreeModel model;

	public ResultTreePanel(DefaultTreeModel model) {
		super();
		this.model = model;

		createTree();
		setLayout(new GridLayout());
		add(new JScrollPane(tree));

		Dimension size = new Dimension(300, 400);
		setMinimumSize(size);
		setPreferredSize(size);
	}

	public void select(SourceTreeNode node) {
		tree.setSelectionPath(new TreePath(model.getPathToRoot(node)));
	}

	private void createTree() {
		tree = new JTree();
		tree.setModel(model);
		tree.addTreeExpansionListener(this);
		tree.setCellRenderer(new ResultTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public void addTreeSelectionListener(TreeSelectionListener listener) {
		tree.addTreeSelectionListener(listener);
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		TreeNode expandedNode = (TreeNode) event.getPath().getLastPathComponent();
		TreeNode toScroll = expandedNode.isLeaf() ? expandedNode : expandedNode.getChildAt(0);
		tree.scrollPathToVisible(new TreePath(model.getPathToRoot(toScroll)));
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		TreeNode collapsedNode = (TreeNode) event.getPath().getLastPathComponent();
		tree.scrollPathToVisible(new TreePath(model.getPathToRoot(collapsedNode)));
	}
}