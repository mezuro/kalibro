package org.kalibro.desktop;

import java.util.List;

import javax.swing.JDesktopPane;

import org.kalibro.core.model.BaseTool;
import org.kalibro.desktop.swingextension.InternalFrame;

public class BaseToolController extends CrudController<BaseTool> {

	private CrudController<BaseTool> mock;

	public BaseToolController(JDesktopPane desktopPane, CrudController<BaseTool> mock) {
		super(desktopPane, "Base tool");
		this.mock = mock;
	}

	@Override
	protected BaseTool createEntity(String name) {
		return mock.createEntity(name);
	}

	@Override
	protected List<String> getEntityNames() {
		return mock.getEntityNames();
	}

	@Override
	protected BaseTool getEntity(String name) {
		return mock.getEntity(name);
	}

	@Override
	protected InternalFrame<BaseTool> createFrameFor(BaseTool baseTool) {
		return mock.createFrameFor(baseTool);
	}

	@Override
	protected void removeEntity(String name) {
		mock.removeEntity(name);
	}

	@Override
	protected void save(BaseTool baseTool) {
		mock.save(baseTool);
	}

	@Override
	protected void setEntityName(BaseTool baseTool, String name) {
		mock.setEntityName(baseTool, name);
	}
}