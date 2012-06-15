package org.kalibro.desktop;

import static javax.swing.JOptionPane.*;

import java.awt.Point;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.desktop.swingextension.InternalFrame;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.dialog.InputDialog;
import org.kalibro.desktop.swingextension.dialog.MessageDialog;

public abstract class CrudController<T extends AbstractEntity<T>> extends InternalFrameAdapter {

	private JDesktopPane desktopPane;
	private String entityName;

	public CrudController(JDesktopPane desktopPane, String entityName) {
		this.desktopPane = desktopPane;
		this.entityName = entityName;
	}

	// XXX Interface

	public void newEntity() {
		InputDialog inputDialog = new InputDialog("New " + lowerEntityName(), desktopPane);
		if (inputDialog.userTyped(entityName + " name:"))
			newEntity(inputDialog.getInput());
	}

	public void open() {
		String chosen = chooseEntity("Open " + lowerEntityName());
		if (chosen != null)
			addFrameFor(getEntity(chosen));
	}

	public void delete() {
		String chosen = chooseEntity("Delete " + lowerEntityName());
		if (chosen != null)
			removeEntity(chosen);
	}

	public void save() {
		save(selectedEntity());
	}

	public void saveAs() {
		InputDialog inputDialog = new InputDialog("Save " + lowerEntityName() + " as...", desktopPane);
		if (! inputDialog.userTyped(entityName + " name:"))
			return;
		T entity = selectedEntity();
		setEntityName(entity, inputDialog.getInput());
		save(entity);
		addFrameFor(entity);
	}

	public void close() {
		if (unmodified())
			desktopPane.getSelectedFrame().dispose();
		else
			confirmClose();
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent event) {
		close();
	}

	// XXX Utils

	private void newEntity(String name) {
		if (getEntityNames().contains(name))
			new MessageDialog(entityName + " exists", desktopPane).show(entityName + " '" + name + "' already exists");
		else
			addFrameFor(createEntity(name));
	}

	private String chooseEntity(String title) {
		List<String> names = getEntityNames();
		ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>(title, desktopPane);
		if (noEntity(names) || ! choiceDialog.choose("Select " + lowerEntityName() + ":", names))
			return null;
		return choiceDialog.getChoice();
	}

	private boolean noEntity(List<String> names) {
		if (names.isEmpty()) {
			new MessageDialog("No " + lowerEntityName(), desktopPane).show("No " + lowerEntityName() + " found");
			return true;
		}
		return false;
	}

	private void addFrameFor(T entity) {
		InternalFrame<T> frame = createFrameFor(entity);
		desktopPane.add(frame);
		frame.addInternalFrameListener(this);
		frame.setLocation(newLocation());
		frame.select();
	}

	private Point newLocation() {
		JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
		if (selectedFrame == null)
			return new Point(0, 0);
		Point selectedLocation = selectedFrame.getLocation();
		return new Point(selectedLocation.x + 20, selectedLocation.y + 20);
	}

	private boolean unmodified() {
		T entity = selectedEntity();
		String name = entity.toString();
		return getEntityNames().contains(name) && getEntity(name).deepEquals(entity);
	}

	private T selectedEntity() {
		InternalFrame<T> selectedFrame = (InternalFrame<T>) desktopPane.getSelectedFrame();
		return selectedFrame.get();
	}

	private void confirmClose() {
		String name = selectedEntity().toString();
		JInternalFrame frame = desktopPane.getSelectedFrame();
		String message = entityName + " '" + name + "' has been modified. Save changes?";
		int answer = showConfirmDialog(frame, message, "Save " + lowerEntityName(), YES_NO_CANCEL_OPTION);
		if (answer != CANCEL_OPTION) {
			if (answer == YES_OPTION)
				save();
			frame.dispose();
		}
	}

	private String lowerEntityName() {
		return entityName.toLowerCase();
	}

	// XXX Abstracts

	protected abstract T createEntity(String name);

	protected abstract List<String> getEntityNames();

	protected abstract T getEntity(String name);

	protected abstract InternalFrame<T> createFrameFor(T entity);

	protected abstract void removeEntity(String name);

	protected abstract void save(T entity);

	protected abstract void setEntityName(T entity, String name);
}