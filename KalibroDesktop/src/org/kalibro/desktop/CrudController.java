package org.kalibro.desktop;

import java.util.Collection;

import org.kalibro.core.Identifier;
import org.kalibro.core.reflection.MethodReflector;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.dialog.MessageDialog;
import org.kalibro.desktop.swingextension.panel.EditPanel;

public abstract class CrudController<T> {

	private KalibroFrame frame;

	private MethodReflector reflector;
	private Identifier className;

	protected CrudController(KalibroFrame frame) {
		this.frame = frame;
		reflector = new MethodReflector(entityClass());
		className = Identifier.fromClassName(entityClass().getSimpleName());
	}

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
		if (all.isEmpty())
			new MessageDialog(frame, title).show("No " + className.asText().toLowerCase() + " found.");
		else
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