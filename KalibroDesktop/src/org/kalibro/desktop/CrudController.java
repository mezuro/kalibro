package org.kalibro.desktop;

import java.util.Collection;

import org.kalibro.core.Identifier;
import org.kalibro.core.reflection.MethodReflector;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public abstract class CrudController<T> {

	private KalibroFrame frame = KalibroFrame.getInstance();
	private Identifier className = Identifier.fromClassName(entityClass().getSimpleName());
	private MethodReflector reflector = new MethodReflector(entityClass());

	Identifier getClassName() {
		return className;
	}

	void create() throws Exception {
		addTabFor(entityClass().newInstance());
	}

	void open() {
		T entity = choose("Open");
		if (entity != null)
			addTabFor(entity);
	}

	private void addTabFor(T entity) {
		String title = entity + " - " + className.asText();
		frame.addTab(title, panelFor(entity));
	}

	void delete() {
		T entity = choose("Delete");
		if (entity != null)
			reflector.invoke(entity, "delete");
	}

	private T choose(String title) {
		Collection<T> all = (Collection<T>) reflector.invoke("all");
		ChoiceDialog<T> dialog = new ChoiceDialog<T>(frame, title);
		dialog.choose("Select " + className.asText().toLowerCase() + ":", all);
		return dialog.getChoice();
	}

	void save() {
		EditPanel<T> panel = frame.getSelectedTab();
		T entity = panel.get();
		reflector.invoke(entity, "save");
		frame.setSelectedTitle(entity + " - " + className.asText());
	}

	void close() {
		frame.removeSelectedTab();
	}

	protected abstract Class<T> entityClass();

	protected abstract EditPanel<T> panelFor(T entity);
}