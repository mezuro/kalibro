package org.kalibro.desktop.swingextension.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;

public class ReflectionMenuItem extends MenuItem implements ActionListener {

	private Method method;
	private Object target;

	public ReflectionMenuItem(String name, String text, int mnemonic, Object target, String methodName) {
		super(name, text, mnemonic);
		this.target = target;
		initializeMethod(methodName);
		addActionListener(this);
	}

	private void initializeMethod(String methodName) {
		try {
			Class<?> type = (target instanceof Class) ? (Class<?>) target : target.getClass();
			method = type.getMethod(methodName);
		} catch (Exception exception) {
			throw new KalibroError("ReflectionMenuItem did not found method on target.", exception);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			method.invoke(target);
		} catch (Exception exception) {
			throw new KalibroException("Error invoking target method.", exception);
		}
	}
}