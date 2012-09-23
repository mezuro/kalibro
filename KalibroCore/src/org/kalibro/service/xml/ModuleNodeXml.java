package org.kalibro.service.xml;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kalibro.core.model.ModuleNode;
import org.kalibro.dto.DataTransferObject;

@XmlRootElement(name = "moduleNode")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleNodeXml extends DataTransferObject<ModuleNode> {

	private ModuleXml module;

	@XmlElement(name = "child")
	private Collection<ModuleNodeXml> children;

	public ModuleNodeXml() {
		super();
	}

	public ModuleNodeXml(ModuleNode moduleNode) {
		module = new ModuleXml(moduleNode.getModule());
		initializeChildren(moduleNode);
	}

	private void initializeChildren(ModuleNode moduleNode) {
		children = new ArrayList<ModuleNodeXml>();
		for (ModuleNode child : moduleNode.getChildren())
			children.add(new ModuleNodeXml(child));
	}

	@Override
	public ModuleNode convert() {
		ModuleNode moduleNode = new ModuleNode(module.convert());
		convertChildren(moduleNode);
		return moduleNode;
	}

	private void convertChildren(ModuleNode moduleNode) {
		if (children != null)
			for (ModuleNodeXml child : children)
				moduleNode.addChild(child.convert());
	}
}