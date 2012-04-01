package org.kalibro.desktop.swingextension.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.kalibro.KalibroException;

public class ReflectionMenuItem extends MenuItem implements ActionListener {

	private Method method;
	private Object controller;

	public ReflectionMenuItem(String name, String text, int mnemonic, Object controller, String methodName) {
		super(name, text, mnemonic);
		this.controller = controller;
		initializeMethod(methodName);
		addActionListener(this);
	}

	private void initializeMethod(String methodName) {
		try {
			method = controller.getClass().getMethod(methodName);
		} catch (NoSuchMethodException exception) {
			throw new KalibroException("ReflectionMenuItem did not found method on controller", exception);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			method.invoke(controller);
		} catch (IllegalAccessException exception) {
			throw new KalibroException("Could not access controller method", exception);
		} catch (InvocationTargetException exception) {
			throw (RuntimeException) exception.getTargetException();
		}
	}
}