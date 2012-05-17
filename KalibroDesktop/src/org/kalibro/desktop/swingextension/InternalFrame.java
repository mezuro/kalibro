package org.kalibro.desktop.swingextension;

import java.awt.Container;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import org.kalibro.KalibroException;
import org.kalibro.desktop.swingextension.icon.Icon;

public abstract class InternalFrame<T> extends JInternalFrame {

	protected T entity;

	public InternalFrame(String name, String title, T entity) {
		super(title, true, true, true, true);
		new Icon(Icon.KALIBRO).replaceIconOf(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setName(name);

		this.entity = entity;
		setContentPane(buildContentPane());
		adjustSize();
		setVisible(true);
	}

	protected abstract Container buildContentPane();

	protected void adjustSize() {
		pack();
		setMinimumSize(getPreferredSize());
		setSize(getPreferredSize());
	}

	public T get() {
		return entity;
	}

	public void select() {
		try {
			setSelected(true);
		} catch (PropertyVetoException exception) {
			throw new KalibroException("Could not select " + getName() + " frame: " + entity, exception);
		}
	}
}