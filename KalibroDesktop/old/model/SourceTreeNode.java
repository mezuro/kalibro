package org.kalibro.desktop.old.model;

import java.util.*;

import javax.swing.tree.TreeNode;

import org.kalibro.Module;
import org.kalibro.ModuleNode;
import org.kalibro.Granularity;

public class SourceTreeNode implements TreeNode {

	private ModuleNode node;
	private SourceTreeNode parent;

	public SourceTreeNode(ModuleNode node, SourceTreeNode parent) {
		this.node = node;
		this.parent = parent;
	}

	public Module getModule() {
		return node.getModule();
	}

	@Override
	public String toString() {
		return node.toString();
	}

	@Override
	public boolean equals(Object other) {
		try {
			return ((SourceTreeNode) other).node.equals(this.node);
		} catch (Exception exception) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	private class ResultNodeEnumeration implements Enumeration<SourceTreeNode> {

		private Iterator<ModuleNode> iterator;
		private SourceTreeNode parentNode;

		public ResultNodeEnumeration(Collection<ModuleNode> nodes, SourceTreeNode parentNode) {
			iterator = nodes.iterator();
			this.parentNode = parentNode;
		}

		@Override
		public boolean hasMoreElements() {
			return iterator.hasNext();
		}

		@Override
		public SourceTreeNode nextElement() {
			return new SourceTreeNode(iterator.next(), parentNode);
		}
	}

	@Override
	public Enumeration<SourceTreeNode> children() {
		return new ResultNodeEnumeration(node.getChildren(), this);
	}

	@Override
	public boolean getAllowsChildren() {
		return node.getModule().getGranularity() != Granularity.METHOD;
	}

	@Override
	public SourceTreeNode getChildAt(int childIndex) {
		List<ModuleNode> children = new ArrayList<ModuleNode>(node.getChildren());
		return new SourceTreeNode(children.get(childIndex), this);
	}

	@Override
	public int getChildCount() {
		return node.getChildren().size();
	}

	@Override
	public int getIndex(TreeNode child) {
		SourceTreeNode childNode = (SourceTreeNode) child;
		List<ModuleNode> children = new ArrayList<ModuleNode>(node.getChildren());
		return children.indexOf(childNode.getModule());
	}

	@Override
	public SourceTreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return node.getChildren().isEmpty();
	}
}